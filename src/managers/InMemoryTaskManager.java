package managers;

import enums.TaskType;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    protected int id;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    protected final Comparator<Task> taskComparator =
                    Comparator.comparing(Task::getStartTime, Comparator.nullsFirst(Comparator.naturalOrder()))
                              .thenComparing(Task::getId);
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(taskComparator);

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
        for (Task taskToDelete : tasks.values()) {
            prioritizedTasks.removeIf(task -> task.equals(taskToDelete));
        }
        tasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            prioritizedTasks.removeIf(task -> task.equals(subtask));
        }
        subtasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (Epic epic : epics.values()) {
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
                prioritizedTasks.removeIf(task -> task.equals(subtask));
            }
        }
        epics.clear();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (!(task == null)) {
            addToHistory(task);
        }
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (!(subtask == null)) {
            addToHistory(subtask);
        }
        return subtask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (!(epic == null)) {
            addToHistory(epic);
        }
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
        if (checkTaskTimeCrossing(task)) {
            System.out.printf("Задача %s не добавлена из-за пересечения с другими задачами%n",
                                    task.getName());
            return;
        }
        task.setId(++this.id);
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void add(Subtask subtask) {
        if (checkTaskTimeCrossing(subtask)) {
            System.out.printf("Задача %s не добавлена из-за пересечения с другими задачами%n",
                    subtask.getName());
            return;
        }
        subtask.setId(++this.id);
        subtasks.put(subtask.getId(), subtask);
        prioritizedTasks.add(subtask);
        int epicId = subtask.getEpicId();
        if (epicId != 0) {
            Epic epic = getEpic(epicId);
            epic.addSubtask(subtask);
            epic.setActualStatus();
            epic.setTime();
        }
    }

    @Override
    public void add(Epic epic) {
        if (checkTaskTimeCrossing(epic)) {
            System.out.printf("Задача %s не добавлена из-за пересечения с другими задачами%n",
                    epic.getName());
            return;
        }
        epic.setId(++this.id);
        epics.put(epic.getId(), epic);
        epic.setActualStatus();
        epic.setTime();
    }
    @Override
    public void update(Task task) {
        if (checkTaskTimeCrossing(task)) {
            System.out.printf("Задача %s не обновлена из-за пересечения с другими задачами%n",
                    task.getName());
            return;
        }
        tasks.put(task.getId(), task);
        prioritizedTasks.removeIf(prioritizedTask -> prioritizedTask.equals(task));
        prioritizedTasks.add(task);
    }

    @Override
    public void update(Subtask subtask) {
        if (checkTaskTimeCrossing(subtask)) {
            System.out.printf("Задача %s не обновлена из-за пересечения с другими задачами%n",
                    subtask.getName());
            return;
        }
        subtasks.put(subtask.getId(), subtask);
        prioritizedTasks.removeIf(prioritizedTask -> prioritizedTask.equals(subtask));
        prioritizedTasks.add(subtask);
        int epicId = subtask.getEpicId();
        if (epicId != 0) {
            Epic epic = getEpic(epicId);
            epic.setActualStatus();
            epic.setTime();
            prioritizedTasks.removeIf(prioritizedTask -> prioritizedTask.equals(epic));
            prioritizedTasks.add(epic);
        }
    }

    @Override
    public void update(Epic epic) {
        if (checkTaskTimeCrossing(epic)) {
            System.out.printf("Задача %s не обновлена из-за пересечения с другими задачами%n",
                    epic.getName());
            return;
        }
        epics.put(epic.getId(), epic);
        epic.setActualStatus();
        epic.setTime();
    }

    @Override
    public void removeFromHistory(Task task) {
        inMemoryHistoryManager.remove(task);
    }

    @Override
    public void deleteTaskById(int id) {
        Task taskToDelete = getTaskById(id);
        if (taskToDelete == null) return;
        removeFromHistory(taskToDelete);
        tasks.remove(id);
        prioritizedTasks.removeIf(task -> task.equals(taskToDelete));
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = getSubtaskById(id);
        if (subtask == null) return;
        removeFromHistory(subtask);
        subtasks.remove(id);
        prioritizedTasks.removeIf(task -> task.equals(subtask));
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = getEpicById(id);
        if (epic == null) return;
        for (Subtask subtask : epic.getSubtasks()) {
            removeFromHistory(subtask);
            subtasks.remove(subtask.getId());
            prioritizedTasks.removeIf(task -> task.equals(subtask));
        }
        removeFromHistory(epic);
        epics.remove(id);
    }

    @Override
    public List<Subtask> getSubtasksOfEpic(int id) {
        Epic epic = getEpic(id);
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
                    epic.setTime();
                }
                break;
        }
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return this.prioritizedTasks;
    }

    public boolean checkTaskTimeCrossing(Task task) {
        LocalDateTime taskEndTime = task.getEndTime();
        LocalDateTime taskStartTime = task.getStartTime();
        if (prioritizedTasks.isEmpty() || taskEndTime == null) return false;
        for (Task prioritizedTask : prioritizedTasks) {
            if (task.equals(prioritizedTask)) continue;
            LocalDateTime prioritizedTaskStartTime = prioritizedTask.getStartTime();
            LocalDateTime prioritizedTaskEndTime = prioritizedTask.getEndTime();
            if (!(prioritizedTaskStartTime == null) && !(taskStartTime == null) &&
                    taskEndTime.isAfter(prioritizedTaskStartTime) &&
                        taskStartTime.isBefore(prioritizedTaskEndTime)) {
                return true;
            }
        }
        return false;
    }
}
