package test;

import http.KVServer;
import http.KVTaskClient;
import managers.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    KVServer server;
    KVTaskClient client;

    {
        try {
            server = new KVServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setUp() {
        server.start();
        System.out.println("Сервер хранилища запущен");
        taskManager = new HttpTaskManager();
        initializeTasks();
    }

    @AfterEach
    public void tearDown() {
        server.stop();
        System.out.println("Сервер хранилища остановлен");
    }

    @Test
    void loadFromServer() {
        taskManager.load();
        HashMap<Integer, Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Менеджер не вернул список задач");
        assertEquals(1, tasks.size(), "Менеджер вернул пустой список задач");
        HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks, "Менеджер не вернул список подзадач");
        assertEquals(1, subtasks.size(), "Менеджер вернул пустой список подзадач");
        HashMap<Integer, Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Менеджер не вернул список эпиков");
        assertEquals(1, epics.size(), "Менеджер вернул пустой список эпиков");
    }

    @Test
    void loadFromServerNoAnyTasks() {
        taskManager.deleteTasks();
        taskManager.deleteSubtasks();
        taskManager.deleteEpics();
        taskManager.load();
        HashMap<Integer, Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Менеджер не вернул список задач");
        assertEquals(0, tasks.size(), "Менеджер вернул не пустой список задач");
        HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks, "Менеджер не вернул список подзадач");
        assertEquals(0, subtasks.size(), "Менеджер вернул не пустой список подзадач");
        HashMap<Integer, Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Менеджер не вернул список эпиков");
        assertEquals(0, epics.size(), "Менеджер вернул не пустой список эпиков");
    }

    @Test
    public void loadFromServerEpicWithoutSubtasks() {
        taskManager.deleteSubtasks();
        HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(0, subtasks.size(), "Список подзадач не пустой");
        HttpTaskManager newTaskManager = new HttpTaskManager();
        newTaskManager.load();
        final HashMap<Integer, Subtask> subtasksFromServer = newTaskManager.getSubtasks();
        final HashMap<Integer, Epic> epicsFromServer = newTaskManager.getEpics();
        assertEquals(0, subtasksFromServer.size(), "Список подзадач не пустой");
        assertEquals(1, epicsFromServer.size(), "Список эпиков пустой");
    }

    @Test
    public void loadFromFileEmptyHistory() {
        taskManager.addToHistory(task);
        taskManager.addToHistory(subtask);
        taskManager.addToHistory(epic);
        HttpTaskManager newTaskManager = new HttpTaskManager();
        newTaskManager.load();
        List<Task> historyFromServer = newTaskManager.getHistory();
        assertEquals(3, historyFromServer.size(), "История из файла пустая");
        taskManager.removeFromHistory(task);
        taskManager.removeFromHistory(subtask);
        taskManager.removeFromHistory(epic);
        newTaskManager = new HttpTaskManager();
        historyFromServer = newTaskManager.getHistory();
        assertEquals(0, historyFromServer.size(), "История из файла не пустая");
    }
}
