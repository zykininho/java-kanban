package test;

import enums.Status;
import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private Epic epic;
    private InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    void setUp() {
        epic = new Epic("Эпик 1", "Для теста",
                60, LocalDateTime.of(2022, 1, 1, 12, 0, 0));
        inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.add(epic);
    }

    public void addSubtasksWithStatus(Status status) {
        Subtask subtask1 = new Subtask("Сабтаск 1", "Для теста", 1,
                                15, LocalDateTime.of(2022, 1, 2, 13, 0, 0));
        subtask1.setStatus(status);
        inMemoryTaskManager.add(subtask1);
        Subtask subtask2 = new Subtask("Сабтаск 2", "Для теста", 1,
                                20, LocalDateTime.of(2022, 1, 3, 14, 0, 0));
        subtask2.setStatus(status);
        inMemoryTaskManager.add(subtask2);
        Subtask subtask3 = new Subtask("Сабтаск 3", "Для теста", 1,
                                25, LocalDateTime.of(2022, 1, 4, 15, 0, 0));
        subtask3.setStatus(status);
        inMemoryTaskManager.add(subtask3);
    }

    @Test
    public void isEpicIdIncreaseAfterCreation() {
        int epicId = epic.getId();
        assertEquals(epicId, 1);
    }

 @Test
    public void checkEpicStatusWithNoSubtasks() {
        List<Subtask> subtasks = epic.getSubtasks();
        assertEquals(0, subtasks.size(), "Список подзадач не пустой");
        epic.setActualStatus();
        assertEquals(Status.NEW, epic.getStatus(), "Статус эпика не NEW");
    }

    @Test
    public void checkEpicStatusWithSubtasksStatusNew() {
        addSubtasksWithStatus(Status.NEW);
        List<Subtask> subtasks = epic.getSubtasks();
        assertEquals(3, subtasks.size(), "Список подзадач пустой");
        epic.setActualStatus();
        assertEquals(Status.NEW, epic.getStatus(), "Статус эпика не NEW");
    }

    @Test
    public void checkEpicStatusWithSubtasksStatusDone() {
        addSubtasksWithStatus(Status.DONE);
        List<Subtask> subtasks = epic.getSubtasks();
        assertEquals(3, subtasks.size(), "Список подзадач пустой");
        epic.setActualStatus();
        assertEquals(Status.DONE, epic.getStatus(), "Статус эпика не DONE");
    }

    @Test
    public void checkEpicStatusWithSubtasksStatusNewAndDone() {
        addSubtasksWithStatus(Status.NEW);
        Subtask subtask = new Subtask("Сабтаск 4", "Для теста", 1,
                30, LocalDateTime.of(2022, 1, 5, 14, 0, 0));
        subtask.setStatus(Status.DONE);
        inMemoryTaskManager.add(subtask);
        List<Subtask> subtasks = epic.getSubtasks();
        assertEquals(4, subtasks.size(), "Список подзадач пустой");
        epic.setActualStatus();
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус эпика не IN_PROGRESS");
    }

    @Test
    public void checkEpicStatusWithSubtasksStatusInProgress() {
        addSubtasksWithStatus(Status.IN_PROGRESS);
        List<Subtask> subtasks = epic.getSubtasks();
        assertEquals(3, subtasks.size(), "Список подзадач пустой");
        epic.setActualStatus();
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус эпика не IN_PROGRESS");
    }
}
