package managers;

import tasks.*;
import java.util.HashMap;
import java.util.List;

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

}
