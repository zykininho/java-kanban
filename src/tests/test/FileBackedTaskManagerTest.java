package test;

import managers.FileBackedTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    void setUp() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        file = new File("tasksTest_" + dateTimeFormatter.format(LocalDateTime.now()) + ".csv");
        taskManager = new FileBackedTaskManager(file);
        initializeTasks();
    }

    @AfterEach
    void tearDown() {
        assertTrue(file.delete());
    }

    @Test
    public void loadFromFile() {
        FileBackedTaskManager taskManager1 = FileBackedTaskManager.loadFromFile(file);
        final HashMap<Integer, Task> tasks = taskManager1.getTasks();
        assertNotNull(tasks, "Возвращает пустой список задач");
        assertEquals(1, tasks.size(), "Возвращает пустой список задач");
    }
}