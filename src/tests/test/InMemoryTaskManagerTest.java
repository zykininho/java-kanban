package test;

import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        initializeTasks();
    }

    /*@Test
    public void createInMemoryTaskManager() {
        assertNotNull(tasks, "Возвращает пустой список задач");
    }*/
}