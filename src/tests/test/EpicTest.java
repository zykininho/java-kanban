package test;

import managers.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    public void isEpicIdIncreaseAfterCreation() {
        Epic epic = new Epic("Эпик", "Для теста");
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.add(epic);
        int epicId = epic.getId();
        assertEquals(epicId, 1);
    }

}