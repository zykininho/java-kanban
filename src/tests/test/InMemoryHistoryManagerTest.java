package test;

import managers.HistoryManager;
import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    protected InMemoryTaskManager taskManager;
    protected HistoryManager historyManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
        historyManager = new InMemoryHistoryManager();
        initializeTasks();
    }

    protected void initializeTasks() {
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

    @Test
    void add() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая");
        assertEquals(1, history.size(), "История не пустая");
    }

    @Test
    void addAndRemoveTaskFromBeginToEnd() {
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая");
        assertEquals(1, history.size(), "История пустая");
        historyManager.add(epic);
        history = historyManager.getHistory();
        assertNotNull(history, "История не пустая");
        assertEquals(2, history.size(), "История пустая");
        historyManager.add(subtask);
        history = historyManager.getHistory();
        assertNotNull(history, "История не пустая");
        assertEquals(3, history.size(), "История пустая");
        historyManager.remove(task);
        history = historyManager.getHistory();
        assertNotNull(history, "История пустая");
        assertEquals(2, history.size(), "История не пустая");
        historyManager.remove(epic);
        history = historyManager.getHistory();
        assertNotNull(history, "История пустая");
        assertEquals(1, history.size(), "История не пустая");
        historyManager.remove(subtask);
        history = historyManager.getHistory();
        assertNotNull(history, "История пустая");
        assertEquals(0, history.size(), "История не пустая");
    }

    @Test
    void getHistoryWithTwiceAddingTask() {
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая");
        assertEquals(1, history.size(), "История пустая");
        historyManager.add(task);
        history = historyManager.getHistory();
        assertNotNull(history, "История не пустая");
        assertEquals(1, history.size(), "История пустая");
    }

    @Test
    void getHistoryWithNoTasks() {
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая");
        assertEquals(0, history.size(), "История не пустая");
    }
}