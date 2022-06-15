import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public long getId() {
        return id++;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteSubtasks() {
        subtasks.clear();
    }

    public void deleteEpics() {
        for (Epic epic : epics.values()) {
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
            }
            epics.remove(epic.getId());
        }
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void add(Task task) {
        task.id = ++this.id;
        tasks.put(task.getId(), task);
    }

    public void add(Subtask subtask) {
        subtask.id = ++this.id;
        subtasks.put(subtask.getId(), subtask);
        int epicId = subtask.getEpicId();
        if (!(epicId == 0)) {
            Epic epic = getEpicById(epicId);
            epic.setActualStatus();
        }
    }

    public void add(Epic epic) {
        epic.id = ++this.id;
        epics.put(epic.getId(), epic);
        epic.setActualStatus();
    }

    public void update(Task task) {
        tasks.put(task.getId(), task);
    }

    public void update(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        int epicId = subtask.getEpicId();
        if (!(epicId == 0)) {
            Epic epic = getEpicById(epicId);
            epic.setActualStatus();
        }
    }

    public void update(Epic epic) {
        epics.put(epic.getId(), epic);
        epic.setActualStatus();
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteSubtaskById(int id) {
        subtasks.remove(id);
    }

    public void deleteEpicById(int id) {
        epics.remove(id);
    }

    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        return epic.getSubtasks();
    }
}
