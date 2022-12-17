package test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exceptions.ManagerSaveException;
import http.HttpTaskServer;
import managers.HistoryManager;
import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;
import managers.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    protected InMemoryTaskManager taskManager = new InMemoryTaskManager();
    protected HistoryManager historyManager = taskManager.getInMemoryHistoryManager();
    protected HttpTaskServer server;
    protected Gson gson = Managers.getGson();
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    {
        try {
            server = new HttpTaskServer(taskManager);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @BeforeEach
    void setUp() {
        server.start();
        System.out.println("Сервер для работы с Task запущен");
        initializeTasks();
    }

    @AfterEach
    void tearDown() {
        server.stop();
        System.out.println("Сервер для работы с Task остановлен");
    }

    void initializeTasks() {
        task = new Task("Таск 1", "Для теста", 15,
                LocalDateTime.of(2022, 01, 01, 12, 00, 00));
        taskManager.add(task);
        epic = new Epic("Эпик 1", "Для теста",60,
                LocalDateTime.of(2022, 01, 02, 13, 00, 00));
        taskManager.add(epic);
        subtask = new Subtask("Сабтаск 1", "Для теста", epic.getId(),
                30, LocalDateTime.of(2022, 01, 03, 14, 00, 00));
        taskManager.add(subtask);
    }

    // TASK

    @Test
    public void getTasks() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = httpResponse.statusCode();
        assertEquals(200, statusCode, "Запрос получения списка задач вернул ошибку");
        String body = httpResponse.body();
        List<Task> tasks = gson.fromJson(body, new TypeToken<ArrayList<Task>>() {}.getType());
        assertNotNull(tasks, "Сервер не вернул список задач");
        assertEquals(1, tasks.size(), "Количество задач в списках не равно");
    }

    @Test
    public void getTaskById() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task?id=1"))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = httpResponse.statusCode();
        assertEquals(200, statusCode, "Запрос получения задачи по id вернул ошибку");
        String body = httpResponse.body();
        Task task = gson.fromJson(body, Task.class);
        assertNotNull(task, "Сервер не вернул задачу по id");
        assertEquals(this.task, task, "Задачи не равны");
    }

    @Test
    public void createTask() throws IOException, InterruptedException {
        taskManager.deleteTasks();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();
        HttpResponse<Void> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        int statusCode = httpResponse.statusCode();
        assertEquals(200, statusCode, "Запрос создания задачи вернул ошибку");
        List<Task> tasks = new ArrayList<>(taskManager.getTasks().values());
        Task keyTask = tasks.get(0);
        assertEquals(this.task, task, "Задачи не равны");
    }

    @Test
    public void deleteTask() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task?id=1"))
                .DELETE()
                .build();
        HttpResponse<Void> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        int statusCode = httpResponse.statusCode();
        assertEquals(200, statusCode, "Запрос удаления задачи вернул ошибку");
        List<Task> tasks = new ArrayList<>(taskManager.getTasks().values());
        assertEquals(0, tasks.size(), "Задача не удалена");
    }

    @Test
    public void deleteTasks() throws IOException, InterruptedException {
        Task task2 = new Task("Таск 2", "Для теста", 15,
                LocalDateTime.of(2022, 02, 01, 13, 00, 00));
        taskManager.add(task2);
        List<Task> tasks2 = new ArrayList<>(taskManager.getTasks().values());
        assertEquals(2, tasks2.size(), "Новая задача не добавлена");
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .DELETE()
                .build();
        HttpResponse<Void> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        int statusCode = httpResponse.statusCode();
        assertEquals(200, statusCode, "Запрос удаления задач вернул ошибку");
        List<Task> tasks = new ArrayList<>(taskManager.getTasks().values());
        assertEquals(0, tasks.size(), "Задачи не удалены");
    }

    // EPIC

    @Test
    public void getEpics() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic"))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = httpResponse.statusCode();
        assertEquals(200, statusCode, "Запрос получения списка эпиков вернул ошибку");
        String body = httpResponse.body();
        List<Epic> epics = gson.fromJson(body, new TypeToken<ArrayList<Epic>>() {}.getType());
        assertNotNull(epics, "Сервер не вернул список эпиков");
        assertEquals(1, epics.size(), "Количество эпиков в списках не равно");
    }

    @Test
    public void getEpicById() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic?id=2"))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = httpResponse.statusCode();
        assertEquals(200, statusCode, "Запрос получения эпика по id вернул ошибку");
        String body = httpResponse.body();
        Epic epic = gson.fromJson(body, Epic.class);
        assertNotNull(epic, "Сервер не вернул эпик по id");
        assertEquals(this.epic, epic, "Эпики не равны");
    }

    @Test
    public void createEpic() throws IOException, InterruptedException {
        taskManager.deleteEpics();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();
        HttpResponse<Void> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        int statusCode = httpResponse.statusCode();
        assertEquals(200, statusCode, "Запрос создания эпика вернул ошибку");
        List<Epic> epics = new ArrayList<>(taskManager.getEpics().values());
        Epic keyEpic = epics.get(0);
        keyEpic.setId(2);
        assertEquals(epic, keyEpic, "Эпики не равны");
    }

    @Test
    public void deleteEpic() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic?id=2"))
                .DELETE()
                .build();
        HttpResponse<Void> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        int statusCode = httpResponse.statusCode();
        assertEquals(200, statusCode, "Запрос удаления эпика вернул ошибку");
        List<Epic> epics = new ArrayList<>(taskManager.getEpics().values());
        assertEquals(0, epics.size(), "Эпик не удален");
    }

    @Test
    public void deleteEpics() throws IOException, InterruptedException {
        Epic epic2 = new Epic("Эпик 2", "Для теста",60,
                LocalDateTime.of(2022, 04, 02, 13, 00, 00));
        taskManager.add(epic2);
        List<Epic> epics2 = new ArrayList<>(taskManager.getEpics().values());
        assertEquals(2, epics2.size(), "Новый эпик не добавлен");
        assertNotEquals(epic.getId(), epic2.getId(), "id у эпиков равны");
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic"))
                .DELETE()
                .build();
        HttpResponse<Void> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        int statusCode = httpResponse.statusCode();
        assertEquals(200, statusCode, "Запрос удаления эпиков вернул ошибку");
        List<Epic> epics = new ArrayList<>(taskManager.getEpics().values());
        assertEquals(0, epics.size(), "Эпики не удалены");
    }

    // SUBTASK

    @Test
    public void getSubtasks() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask"))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = httpResponse.statusCode();
        assertEquals(200, statusCode, "Запрос получения списка подзадач вернул ошибку");
        String body = httpResponse.body();
        List<Subtask> subtasks = gson.fromJson(body, new TypeToken<ArrayList<Subtask>>() {}.getType());
        assertNotNull(subtasks, "Сервер не вернул список подзадач");
        assertEquals(1, subtasks.size(), "Количество подзадач в списках не равно");
    }

    @Test
    public void getSubtaskById() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask?id=3"))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = httpResponse.statusCode();
        assertEquals(200, statusCode, "Запрос получения подзадачи по id вернул ошибку");
        String body = httpResponse.body();
        Subtask subtask = gson.fromJson(body, Subtask.class);
        assertNotNull(subtask, "Сервер не вернул задачу по id");
        assertEquals(this.subtask, subtask, "Задачи не равны");
    }

    @Test
    public void createSubtask() throws IOException, InterruptedException {
        taskManager.deleteSubtasks();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                .build();
        HttpResponse<Void> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        int statusCode = httpResponse.statusCode();
        assertEquals(200, statusCode, "Запрос создания подзадачи вернул ошибку");
        List<Subtask> subtasks = new ArrayList<>(taskManager.getSubtasks().values());
        Task keySubtask = subtasks.get(0);
        keySubtask.setId(3);
        assertEquals(subtask, keySubtask, "Задачи не равны");
    }

    @Test
    public void deleteSubtask() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask?id=3"))
                .DELETE()
                .build();
        HttpResponse<Void> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        int statusCode = httpResponse.statusCode();
        assertEquals(200, statusCode, "Запрос удаления подзадачи вернул ошибку");
        List<Subtask> subtasks = new ArrayList<>(taskManager.getSubtasks().values());
        assertEquals(0, subtasks.size(), "Подзадача не удалена");
    }

    @Test
    public void deleteSubtasks() throws IOException, InterruptedException {
        Subtask subtask2 = new Subtask("Сабтаск 2", "Для теста", epic.getId(),
                30, LocalDateTime.of(2022, 03, 03, 14, 00, 00));
        taskManager.add(subtask2);
        List<Subtask> subtasks2 = new ArrayList<>(taskManager.getSubtasks().values());
        assertEquals(2, subtasks2.size(), "Новая подзадача не добавлена");
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask"))
                .DELETE()
                .build();
        HttpResponse<Void> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        int statusCode = httpResponse.statusCode();
        assertEquals(200, statusCode, "Запрос удаления подзадач вернул ошибку");
        List<Subtask> subtasks = new ArrayList<>(taskManager.getSubtasks().values());
        assertEquals(0, subtasks.size(), "Задачи не удалены");
    }

    @Test
    public void getPrioritizedTasks() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/"))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = httpResponse.statusCode();
        assertEquals(200, statusCode, "Запрос получения упорядоченного списка задач вернул ошибку");
        String body = httpResponse.body();
        assertNotEquals("", body, "Запрос вернул пустой упорядоченный список");
    }

    @Test
    public void getHistory() throws IOException, InterruptedException {
        historyManager.add(task);
        historyManager.add(subtask);
        historyManager.add(epic);
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/history"))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = httpResponse.statusCode();
        assertEquals(200, statusCode, "Запрос получения истории задач вернул ошибку");
        String body = httpResponse.body();
        List<Task> history = gson.fromJson(body, new TypeToken<ArrayList<Task>>() {}.getType());
        assertEquals(3, history.size(), "В истории нет задач");
    }

    @Test
    public void getEpicSubtasksById() throws IOException, InterruptedException {
        Subtask subtask2 = new Subtask("Сабтаск 2", "Для теста", epic.getId(),
                30, LocalDateTime.of(2022, 03, 03, 14, 00, 00));
        taskManager.add(subtask2);
        Subtask subtask3 = new Subtask("Сабтаск 3", "Для теста", epic.getId(),
                30, LocalDateTime.of(2022, 04, 03, 14, 00, 00));
        taskManager.add(subtask3);
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask/epic?id=2"))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = httpResponse.statusCode();
        assertEquals(200, statusCode, "Запрос получения подзадач эпика по id вернул ошибку");
        String body = httpResponse.body();
        List<Subtask> subtasks = gson.fromJson(body, new TypeToken<ArrayList<Subtask>>() {}.getType());
        assertNotNull(subtasks, "Сервер не вернул список подзадач эпика по id");
        assertEquals(3, subtasks.size(), "Количество подзадач эпика не совпадает");
    }
}