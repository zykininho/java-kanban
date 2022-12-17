package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import managers.*;
import tasks.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HttpServer server;
    private final TaskManager taskManager;
    private final Gson gson;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handler);
    }

    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        final HttpTaskServer server = new HttpTaskServer();
        server.start();
        server.stop();
        kvServer.stop();
    }

    public void start() {
        System.out.println("Запускаем сервер для работы с Task на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер для работы с Task на порту " + PORT);
    }

    private void handler(HttpExchange exchange) {
        try {
            System.out.println("\n/tasks: " + exchange.getRequestURI());
            final String path = exchange.getRequestURI().getPath().substring(7);
            switch (path) {
                // GET /tasks/ = getPrioritizedTasks()
                case "": {
                    String requestMethod = exchange.getRequestMethod();
                    if (!requestMethod.equals("GET")) {
                        System.out.println("/ Вместо GET-запроса отправили метод: " + requestMethod);
                        exchange.sendResponseHeaders(405, 0);
                    }
                    final String response = gson.toJson(taskManager.getPrioritizedTasks());
                    sendText(exchange, response);
                    break;
                }
                // GET /tasks/history = getHistory()
                case "history": {
                    String requestMethod = exchange.getRequestMethod();
                    if (!requestMethod.equals("GET")) {
                        System.out.println("/ Вместо GET-запроса отправили метод: " + requestMethod);
                        exchange.sendResponseHeaders(405, 0);
                    }
                    final String response = gson.toJson(taskManager.getHistory());
                    sendText(exchange, response);
                    break;
                }
                case "task": {
                    handleTask(exchange);
                    break;
                }
                case "subtask": {
                    handleSubtask(exchange);
                    break;
                }
                // GET /tasks/subtask/epic/?id= = getEpicSubTasks(id)
                case "subtask/epic": {
                    String requestMethod = exchange.getRequestMethod();
                    if (!requestMethod.equals("GET")) {
                        System.out.println("/ Вместо GET-запроса отправили метод: " + requestMethod);
                        exchange.sendResponseHeaders(405, 0);
                    }
                    final String query = exchange.getRequestURI().getQuery();
                    String paramId = query.substring(3); // получаем параметр строки ?id=
                    final int id = getIdFromParam(paramId);
                    final List<Subtask> subtasks = taskManager.getSubtasksOfEpic(id);
                    final String response = gson.toJson(subtasks);
                    System.out.println("Получили подзадачи эпика с id " + id);
                    sendText(exchange, response);
                    break;
                }
                case "epic": {
                    handleEpic(exchange);
                    break;
                }
                default:
                    System.out.println("Запрос неверный " + exchange.getRequestURI());
                    exchange.sendResponseHeaders(404, 0);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handleTask(HttpExchange exchange) throws IOException {
        final String query = exchange.getRequestURI().getQuery();
        switch (exchange.getRequestMethod()) {
            case "GET": {
                // GET /tasks/task/ = getTasks()
                if (query == null) {
                    final Map<Integer, Task> tasks = taskManager.getTasks();
                    List<Task> taskList = new ArrayList<>(tasks.values());
                    final String response = gson.toJson(taskList);
                    System.out.println("Получили список задач");
                    sendText(exchange, response);
                    return;
                }
                // GET /tasks/task/?id= = getTaskById(id)
                String paramId = query.substring(3); // получаем параметр строки ?id=
                final int id = getIdFromParam(paramId);
                final Task task = taskManager.getTaskById(id);
                final String response = gson.toJson(task);
                System.out.println("Получили задачу с id " + id);
                sendText(exchange, response);
                return;
            }
            case "POST": {
                // POST /tasks/task/ Body: {task ..} = addTask(task), updateTask(task)
                if (query == null) {
                    String jsonTask = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                    Task task = gson.fromJson(jsonTask, Task.class);
                    int id = task.getId();
                    if (taskManager.getTasks().containsKey(id)) {
                        taskManager.update(task);
                    } else {
                        taskManager.add(task);
                    }
                    System.out.println("Создали новую задачу");
                    exchange.sendResponseHeaders(200, 0);
                    return;
                }
            }
            case "DELETE": {
                // DELETE /tasks/task/ = deleteAllTasks()
                if (query == null) {
                    taskManager.deleteTasks();
                    System.out.println("Удалили все задачи");
                    exchange.sendResponseHeaders(200, 0);
                    return;
                }
                // DELETE /tasks/task/?id= = deleteTaskById(id)
                String paramId = query.substring(3); // получаем параметр строки ?id=
                final int id = getIdFromParam(paramId);
                taskManager.deleteTaskById(id);
                System.out.println("Удалили задачу с id " + id);
                exchange.sendResponseHeaders(200, 0);
            }
        }
    }

    private void handleEpic(HttpExchange exchange) throws IOException {
        final String query = exchange.getRequestURI().getQuery();
        switch (exchange.getRequestMethod()) {
            case "GET": {
                // GET /tasks/epic/ = getEpics()
                if (query == null) {
                    final Map<Integer, Epic> epics = taskManager.getEpics();
                    List<Task> taskList = new ArrayList<>(epics.values());
                    final String response = gson.toJson(taskList);
                    System.out.println("Получили список эпиков");
                    sendText(exchange, response);
                    return;
                }
                // GET /tasks/epic/?id= = getEpicById(id)
                String paramId = query.substring(3); // получаем параметр строки ?id=
                final int id = getIdFromParam(paramId);
                final Epic epic = taskManager.getEpicById(id);
                final String response = gson.toJson(epic);
                System.out.println("Получили эпик с id " + id);
                sendText(exchange, response);
                return;
            }
            case "POST": {
                // POST /tasks/epic/ Body: {epic ..} = addEpic(epic), updateEpic(epic)
                if (query == null) {
                    String jsonEpic = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                    Epic epic = gson.fromJson(jsonEpic, Epic.class);
                    int id = epic.getId();
                    if (taskManager.getEpics().containsKey(id)) {
                        taskManager.update(epic);
                    } else {
                        taskManager.add(epic);
                    }
                    System.out.println("Создали новый эпик");
                    exchange.sendResponseHeaders(200, 0);
                    return;
                }
            }
            case "DELETE": {
                // DELETE /tasks/epic/ = deleteAllEpics()
                if (query == null) {
                    taskManager.deleteEpics();
                    System.out.println("Удалили все эпики");
                    exchange.sendResponseHeaders(200, 0);
                    return;
                }
                // DELETE /tasks/epic/?id= = deleteEpicById(id)
                String paramId = query.substring(3); // получаем параметр строки ?id=
                final int id = getIdFromParam(paramId);
                taskManager.deleteEpicById(id);
                System.out.println("Удалили эпик с id " + id);
                exchange.sendResponseHeaders(200, 0);
            }
        }
    }

    private void handleSubtask(HttpExchange exchange) throws IOException {
        final String query = exchange.getRequestURI().getQuery();
        switch (exchange.getRequestMethod()) {
            case "GET": {
                // GET /tasks/subtask/ = getSubtasks()
                if (query == null) {
                    final Map<Integer, Subtask> subtasks = taskManager.getSubtasks();
                    List<Task> taskList = new ArrayList<>(subtasks.values());
                    final String response = gson.toJson(taskList);
                    System.out.println("Получили список подзадач");
                    sendText(exchange, response);
                    return;
                }
                // GET /tasks/subtask/?id= = getSubtaskById(id)
                String paramId = query.substring(3); // получаем параметр строки ?id=
                final int id = getIdFromParam(paramId);
                final Subtask subtask = taskManager.getSubtaskById(id);
                final String response = gson.toJson(subtask);
                System.out.println("Получили подзадачу с id " + id);
                sendText(exchange, response);
                return;
            }
            case "POST": {
                // POST /tasks/subtask/ Body: {subtask ..} = addSubtask(subtask), updateSubtask(subtask)
                if (query == null) {
                    String jsonSubtask = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                    Subtask subtask = gson.fromJson(jsonSubtask, Subtask.class);
                    int id = subtask.getId();
                    if (taskManager.getSubtasks().containsKey(id)) {
                        taskManager.update(subtask);
                    } else {
                        taskManager.add(subtask);
                    }
                    System.out.println("Создали новую подзадачу");
                    exchange.sendResponseHeaders(200, 0);
                    return;
                }
            }
            case "DELETE": {
                // DELETE /tasks/subtask/ = deleteAllSubtasks()
                if (query == null) {
                    taskManager.deleteSubtasks();
                    System.out.println("Удалили все подзадачи");
                    exchange.sendResponseHeaders(200, 0);
                    return;
                }
                // DELETE /tasks/subtask/?id= = deleteSubtaskById(id)
                String paramId = query.substring(3); // получаем параметр строки ?id=
                final int id = getIdFromParam(paramId);
                taskManager.deleteSubtaskById(id);
                System.out.println("Удалили подзадачу с id " + id);
                exchange.sendResponseHeaders(200, 0);
            }
        }
    }

    private void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    private int getIdFromParam(String paramId) {
        try {
            return Integer.parseInt(paramId);
        } catch (NumberFormatException exception) {
            return 0;
        }
    }
}
