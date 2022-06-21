package managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import tasks.*;

public class InMemoryTaskManager implements TaskManager {
    private int id;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private final List<Task> taskViewHistory = new ArrayList<>();

    @Override
    public long getId() {
        return id++;
    }

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

    private void addToHistory(Task task) {
        if (taskViewHistory.size() == 10) {
            taskViewHistory.remove(0);
        }
        taskViewHistory.add(task);
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
            Epic epic = getEpicById(epicId);
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
        if (!(epicId == 0)) {
            Epic epic = getEpicById(epicId);
            epic.setActualStatus();
        }
    }

    @Override
    public void update(Epic epic) {
        epics.put(epic.getId(), epic);
        epic.setActualStatus();
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        subtasks.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        epics.remove(id);
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        return epic.getSubtasks();
    }

    @Override
    public List<Task> getHistory() {
        return taskViewHistory;
    }
}
