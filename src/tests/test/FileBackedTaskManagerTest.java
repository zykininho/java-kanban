package test;

import managers.FileBackedTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    protected File file;

    @BeforeEach
    public void setUp() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        file = new File("tasksTest_" + dateTimeFormatter.format(LocalDateTime.now()) + ".csv");
        taskManager = new FileBackedTaskManager(file);
        initializeTasks();
    }

    @AfterEach
    public void tearDown() {
        assertTrue(file.delete());
    }

    @Test
    public void loadFromFile() {
        FileBackedTaskManager taskManager1 = FileBackedTaskManager.loadFromFile(file);
        final HashMap<Integer, Task> tasks = taskManager1.getTasks();
        assertNotNull(tasks, "Возвращает пустой список задач");
        assertEquals(1, tasks.size(), "Возвращает пустой список задач");
    }

    @Test
    public void loadFromFileNoTasks() {
        taskManager.deleteTasks();
        taskManager.deleteSubtasks();
        taskManager.deleteEpics();
        FileBackedTaskManager taskManager1 = FileBackedTaskManager.loadFromFile(file);
        final HashMap<Integer, Task> tasks = taskManager1.getTasks();
        final HashMap<Integer, Subtask> subtasks = taskManager1.getSubtasks();
        final HashMap<Integer, Epic> epics = taskManager1.getEpics();
        assertNotNull(tasks, "Возвращает пустой список задач");
        assertEquals(0, tasks.size(), "Возвращает не пустой список задач");
        assertNotNull(subtasks, "Возвращает пустой список задач");
        assertEquals(0, subtasks.size(), "Возвращает не пустой список подзадач");
        assertNotNull(epics, "Возвращает пустой список задач");
        assertEquals(0, epics.size(), "Возвращает не пустой список эпиков");
    }

    @Test
    public void loadFromFileEpicWithoutSubtasks() {
        taskManager.deleteSubtasks();
        HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(0, subtasks.size(), "Список подзадач не пустой");
        FileBackedTaskManager taskManager1 = FileBackedTaskManager.loadFromFile(file);
        final HashMap<Integer, Subtask> subtasksFromFile = taskManager1.getSubtasks();
        final HashMap<Integer, Epic> epicsFromFile = taskManager1.getEpics();
        assertEquals(0, subtasksFromFile.size(), "Список подзадач из файла не пустой");
        assertEquals(1, epicsFromFile.size(), "Список эпиков из файла пустой");
    }

    @Test
    public void loadFromFileEmptyHistory() {
        taskManager.addToHistory(task);
        taskManager.addToHistory(subtask);
        taskManager.addToHistory(epic);
        FileBackedTaskManager taskManagerFromFile = FileBackedTaskManager.loadFromFile(file);
        List<Task> historyFromFile = taskManagerFromFile.getHistory();
        assertEquals(3, historyFromFile.size(), "История из файла пустая");
        taskManager.removeFromHistory(task);
        taskManager.removeFromHistory(subtask);
        taskManager.removeFromHistory(epic);
        taskManagerFromFile = FileBackedTaskManager.loadFromFile(file);
        historyFromFile = taskManagerFromFile.getHistory();
        assertEquals(0, historyFromFile.size(), "История из файла не пустая");
    }
}