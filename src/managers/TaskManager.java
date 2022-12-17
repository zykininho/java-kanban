package managers;

import tasks.*;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Subtask> getSubtasks();

    HashMap<Integer, Epic> getEpics();

    void deleteTasks();

    void deleteSubtasks();

    void deleteEpics();

    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

    List<Subtask> getSubtasksOfEpic(int id)

    void add(Task task);

    void add(Subtask subtask);

    void add(Epic epic);

    void update(Task task);

    void update(Subtask subtask);

    void update(Epic epic);

    void deleteTaskById(int id);

    void deleteSubtaskById(int id);

    void deleteEpicById(int id);

    List<Task> getHistory();

    void addToHistory(Task task);

    void removeFromHistory(Task task);

    void addFromFile(Task task);

    TreeSet<Task> getPrioritizedTasks();
}
