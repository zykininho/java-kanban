package test;

import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import tasks.Task;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        initializeTasks();
    }
}