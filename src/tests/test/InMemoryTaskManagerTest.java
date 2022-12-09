package test;

import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Subtask;
import tasks.Task;
import java.time.LocalDateTime;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        initializeTasks();
    }

    @Test
    public void checkCrossingTasksTime() {
        Task taskForChecking = new Task("Таск 2", "Для теста пересечения времени", 30,
                LocalDateTime.of(2022, 01, 01, 12, 10, 00));
        boolean isTasksTimeCrossing = taskManager.checkTaskTimeCrossing(taskForChecking);
        assertTrue(isTasksTimeCrossing, "Задачи не пересекаются по времени");
    }

    @Test
    public void checkNotCrossingTasksTime() {
        Subtask subtaskForChecking = new Subtask("Подзадача 2", "Для теста пересечения по времени", 2, 45,
                LocalDateTime.of(2022, 01, 03, 14, 45, 00));
        boolean isTasksTimeCrossing = taskManager.checkTaskTimeCrossing(subtaskForChecking);
        assertFalse(isTasksTimeCrossing, "Задачи пересекаются по времени");
    }

    @Test
    public void tasksPrioritizedByStartTime() {
        TreeSet<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertNotNull(prioritizedTasks, "Менеджер не возвращает список задач по порядку по времени");
        assertEquals(2, prioritizedTasks.size(), "В список не сохраняются задачи");
        Task firstTask = prioritizedTasks.first();
        assertEquals(task, firstTask, "Неверная сортировка задач в списке");
        Task earliestTask = new Task("Задача 2", "Самая ранняя задача по времени", 30,
                LocalDateTime.of(2022, 01, 01, 10, 30, 00));
        taskManager.add(earliestTask);
        prioritizedTasks = taskManager.getPrioritizedTasks();
        firstTask = prioritizedTasks.first();
        assertEquals(earliestTask, firstTask, "Неверная сортировка задач в списке");
        Task lastTask = prioritizedTasks.last();
        assertEquals(subtask, lastTask, "Неверная сортировка задач в списке");
    }
}