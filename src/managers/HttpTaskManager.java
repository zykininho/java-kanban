package managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import enums.TaskType;
import http.KVTaskClient;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTaskManager {

    private final Gson gson;
    private final KVTaskClient client;

    public HttpTaskManager(int port) {
        super();
        gson = Managers.getGson();
        client = new KVTaskClient(port);
    }

    public static void main(String[] args) {
        new HttpTaskManager(8080).start();
    }

    public void start() {
        load();
    }

    private void load() {
        ArrayList<Task> tasks = gson.fromJson(client.load("tasks"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        addTasks(tasks);
        ArrayList<Epic> epics = gson.fromJson(client.load("epics"), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        addTasks(epics);
        ArrayList<Subtask> subtasks = gson.fromJson(client.load("subtasks"), new TypeToken<ArrayList<Subtask>>() {
        }.getType());
        addTasks(subtasks);
        List<Integer> history = gson.fromJson(client.load("history"), new TypeToken<ArrayList<Integer>>() {
        }.getType());
        for (Integer taskId : history) {
            inMemoryHistoryManager.add(findTaskById(taskId));
        }
    }

    private void addTasks(List<? extends Task> tasks) {
        for (Task task : tasks) {
            final int id = task.getId();
            TaskType type = task.getType();
            switch (type) {
                case TASK:
                    this.tasks.put(id, task);
                    prioritizedTasks.add(task);
                    break;
                case SUBTASK:
                    subtasks.put(id, (Subtask) task);
                    prioritizedTasks.add(task);
                    break;
                case EPIC:
                    epics.put(id, (Epic) task);
                    break;
            }
        }
    }

    private Task findTaskById(Integer taskId) {
        for (Task task : tasks.values()) {
            if (task.getId() == taskId) {
                return task;
            }
        }
        for (Epic epic : epics.values()) {
            if (epic.getId() == taskId) {
                return epic;
            }
        }
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getId() == taskId) {
                return subtask;
            }
        }
        return null;
    }

    @Override
    protected void save() {
        String jsonTasks = gson.toJson(new ArrayList<>(tasks.values()));
        client.put("tasks", jsonTasks);
        String jsonSubtasks = gson.toJson(new ArrayList<>(subtasks.values()));
        client.put("subtasks", jsonSubtasks);
        String jsonEpics = gson.toJson(new ArrayList<>(epics.values()));
        client.put("epics", jsonEpics);

        String jsonHistory = gson.toJson(inMemoryHistoryManager.getHistory().stream().map(Task::getId).collect(Collectors.toList()));
        client.put("history", jsonHistory);
    }
}
