package managers;

import enums.TaskType;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int id;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    @Override
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        subtasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (Epic epic : epics.values()) {
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
            }
            epics.remove(epic.getId());
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        addToHistory(task);
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        addToHistory(subtask);
        return subtask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        addToHistory(epic);
        return epic;
    }

    @Override
    public void addToHistory(Task task) {
        inMemoryHistoryManager.add(task);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    @Override
    public void add(Task task) {
        task.setId(++this.id);
        tasks.put(task.getId(), task);
    }

    @Override
    public void add(Subtask subtask) {
        subtask.setId(++this.id);
        subtasks.put(subtask.getId(), subtask);
        int epicId = subtask.getEpicId();
        if (epicId != 0) {
            Epic epic = getEpic(epicId);
            epic.addSubtask(subtask);
            epic.setActualStatus();
        }
    }

    @Override
    public void add(Epic epic) {
        epic.setId(++this.id);
        epics.put(epic.getId(), epic);
        epic.setActualStatus();
    }

    @Override
    public void update(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void update(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        int epicId = subtask.getEpicId();
        if (epicId != 0) {
            Epic epic = getEpic(epicId);
            epic.setActualStatus();
        }
    }

    @Override
    public void update(Epic epic) {
        epics.put(epic.getId(), epic);
        epic.setActualStatus();
    }

    @Override
    public void removeFromHistory(Task task) {
        inMemoryHistoryManager.remove(task);
    }

    @Override
    public void deleteTaskById(int id) {
        removeFromHistory(getTaskById(id));
        tasks.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        removeFromHistory(getSubtaskById(id));
        subtasks.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = getEpicById(id);

        for (Subtask subtask : epic.getSubtasks()) {
            removeFromHistory(subtask);
            subtasks.remove(subtask);
        }
        removeFromHistory(epic);
        epics.remove(id);
    }

    public List<Subtask> getSubtasksOfEpic(Epic epic) {
        return epic.getSubtasks();
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    public HistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

    @Override
    public void addFromFile(Task task) {
        TaskType taskType = task.getType();
        switch (taskType) {
            case TASK:
                tasks.put(task.getId(), task);
                break;
            case EPIC:
                epics.put(task.getId(), (Epic) task);
                break;
            case SUBTASK:
                Subtask subtask = (Subtask) task;
                subtasks.put(task.getId(), subtask);
                int epicId = subtask.getEpicId();
                if (epicId != 0) {
                    Epic epic = getEpic(epicId);
                    epic.addSubtask(subtask);
                    epic.setActualStatus();
                }
                break;
        }
    }
}
