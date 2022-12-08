package test;

import enums.Status;
import managers.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    public void initializeTasks() {
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
    public void addNewTask() {
        int taskId = task.getId();
        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают 1");

        final HashMap<Integer, Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач");
        assertEquals(task, tasks.get(taskId), "Задачи не совпадают 2");
    }

    @Test
    public void addNewSubtask() {
        int subtaskId = subtask.getId();
        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);

        assertNotNull(savedSubtask, "Подзадача не найдена");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают 1");

        final HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Подзадачи не возвращаются");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач");
        assertEquals(subtask, subtasks.get(subtaskId), "Подзадачи не совпадают 2");
    }

    @Test
    public void addNewEpic() {
        int epicId = epic.getId();
        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найден");
        assertEquals(epic, savedEpic, "Эпики не совпадают 1");

        final HashMap<Integer, Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики не возвращаются");
        assertEquals(1, epics.size(), "Неверное количество эпиков");
        assertEquals(epic, epics.get(epicId), "Эпики не совпадают 2");
    }

    @Test
    public void getListWithoutTasks() {
        taskManager.deleteTasks();
        HashMap<Integer, Task> tasks = taskManager.getTasks();
        assertEquals(0, tasks.size(), "Список тасков не пустой");
    }

    @Test
    public void getListWithTasks() {
        HashMap<Integer, Task> tasks = taskManager.getTasks();
        assertEquals(1, tasks.size(), "Список тасков пустой");
    }

    @Test
    public void getListWithoutEpics() {
        taskManager.deleteEpics();
        HashMap<Integer, Epic> epics = taskManager.getEpics();
        assertEquals(0, epics.size(), "Список эпиков не пустой");
    }

    @Test
    public void getListWithEpics() {
        HashMap<Integer, Epic> epics = taskManager.getEpics();
        assertEquals(1, epics.size(), "Список эпиков пустой");
    }

    @Test
    public void getListWithoutSubtasks() {
        taskManager.deleteSubtasks();
        HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(0, subtasks.size(), "Список сабтасков не пустой");
    }

    @Test
    public void getListWithSubtasks() {
        HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(1, subtasks.size(), "Список сабтасков пустой");
    }

    @Test
    public void deleteTasks() {
        taskManager.deleteTasks();
        HashMap<Integer, Task> tasks = taskManager.getTasks();
        assertEquals(0, tasks.size(), "Список тасков после очистки не пустой");
    }

    @Test
    public void deleteSubtasks() {
        taskManager.deleteSubtasks();
        HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(0, subtasks.size(), "Список сабтасков после очистки не пустой");
    }

    @Test
    public void deleteEpics() {
        taskManager.deleteEpics();
        HashMap<Integer, Epic> epics = taskManager.getEpics();
        assertEquals(0, epics.size(), "Список эпиков после очистки не пустой");
    }

    @Test
    public void getTaskByIdNotNull() {
        Task task = taskManager.getTaskById(1);
        assertNotNull(task, "Таск не найден");
    }

    @Test
    public void getTaskByIdIsNull() {
        Task task = taskManager.getTaskById(0);
        assertNull(task, "Таск найден");
    }

    @Test
    public void getSubtaskByIdNotNull() {
        Subtask subtask = taskManager.getSubtaskById(3);
        assertNotNull(subtask, "Сабтаск не найден");
    }

    @Test
    public void getSubtaskByIdIsNull() {
        Subtask subtask = taskManager.getSubtaskById(0);
        assertNull(subtask, "Сабтаск найден");
    }

    @Test
    public void getEpicByIdNotNull() {
        Epic epic = taskManager.getEpicById(2);
        assertNotNull(epic, "Таск не найден");
    }

    @Test
    public void getEpicByIdIsNull() {
        Epic epic = taskManager.getEpicById(0);
        assertNull(epic, "Таск найден");
    }

    // Проверка наличия эпика у подзадач
    @Test
    public void isEpicHasSubtasks() {
        List<Subtask> subtasks = epic.getSubtasks();
        assertNotNull(subtasks, "Менеджер не возвращает список подзадач");
        assertEquals(1, subtasks.size(), "У эпика нет подзадач");
        int epicId = subtask.getEpicId();
        Epic epicById = taskManager.getEpicById(epicId);
        assertNotNull(epicById, "Менеджер не возвращает эпик");
        List<Subtask> savedSubtasks = epicById.getSubtasks();
        Subtask savedSubtask = savedSubtasks.get(0);
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают");
    }

    @Test
    public void changeAndUpdateTasks() {
        task.setName("Таск 1 с новым именем");
        taskManager.update(task);
        String taskName = task.getName();
        assertEquals("Таск 1 с новым именем", taskName, "Название задачи не изменилось");
        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.update(subtask);
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус эпика не обновился");
    }

    @Test
    public void deleteEpicById() {
        taskManager.deleteEpicById(epic.getId());
        HashMap<Integer, Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Менеджер не возвращает эпики");
        assertEquals(0, epics.size(), "Список эпиков не пустой");
    }

    @Test
    public void deleteEpicByWrongId() {
        taskManager.deleteEpicById(0);
        HashMap<Integer, Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Менеджер не возвращает эпики");
        assertEquals(1, epics.size(), "Список эпиков пустой");
    }

    @Test
    public void deleteSubtaskById() {
        taskManager.deleteSubtaskById(subtask.getId());
        HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks, "Менеджер не возвращает подзадачи");
        assertEquals(0, subtasks.size(), "Список подзадач не пустой");
    }

    @Test
    public void deleteSubtaskByWrongId() {
        taskManager.deleteSubtaskById(0);
        HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks, "Менеджер не возвращает подзадачи");
        assertEquals(1, subtasks.size(), "Список подзадач пустой");
    }

    @Test
    public void deleteTaskById() {
        taskManager.deleteTaskById(task.getId());
        HashMap<Integer, Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Менеджер не возвращает задачи");
        assertEquals(0, tasks.size(), "Список задач не пустой");
    }

    @Test
    public void deleteTaskByWrongId() {
        taskManager.deleteTaskById(0);
        HashMap<Integer, Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Менеджер не возвращает задачи");
        assertEquals(1, tasks.size(), "Список задач пустой");
    }

    @Test
    public void addTaskToHistory() {
        taskManager.addToHistory(task);
        List<Task> history = taskManager.getHistory();
        assertNotNull(history, "Менеджер не возвращает историю");
        assertEquals(1, history.size(), "Задачи нет в истории");
        taskManager.addToHistory(epic);
        history = taskManager.getHistory();
        assertNotNull(history, "Менеджер не возвращает историю");
        assertEquals(2, history.size(), "Задач нет в истории");
        taskManager.addToHistory(subtask);
        history = taskManager.getHistory();
        assertNotNull(history, "Менеджер не возвращает историю");
        assertEquals(3, history.size(), "Задач нет в истории");
    }

    @Test
    public void removeFromHistory() {
        taskManager.addToHistory(task);
        taskManager.addToHistory(epic);
        taskManager.addToHistory(subtask);
        List<Task> history = taskManager.getHistory();
        assertNotNull(history, "Менеджер не возвращает историю");
        assertEquals(3, history.size(), "История задач пустая");
        taskManager.removeFromHistory(task);
        history = taskManager.getHistory();
        assertNotNull(history, "Менеджер не возвращает историю");
        assertEquals(2, history.size(), "История задач пустая");
        taskManager.removeFromHistory(subtask);
        history = taskManager.getHistory();
        assertNotNull(history, "Менеджер не возвращает историю");
        assertEquals(1, history.size(), "История задач пустая");
        taskManager.removeFromHistory(epic);
        history = taskManager.getHistory();
        assertNotNull(history, "Менеджер не возвращает историю");
        assertEquals(0, history.size(), "История задач пустая");
    }

    @Test
    public void getHistoryNoAddingTasks() {
        List<Task> history = taskManager.getHistory();
        assertNotNull(history, "Менеджер не возвращает историю");
        assertEquals(0, history.size(), "История задач пустая");
    }

    @Test
    public void getHistoryWithAddingTasks() {
        taskManager.addToHistory(task);
        taskManager.addToHistory(epic);
        taskManager.addToHistory(subtask);
        List<Task> history = taskManager.getHistory();
        assertNotNull(history, "Менеджер не возвращает историю");
        assertEquals(3, history.size(), "История задач пустая");
    }
}