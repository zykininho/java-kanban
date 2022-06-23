package managers;

import tasks.*;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    public HashMap<Integer, Task> getTasks();

    public HashMap<Integer, Subtask> getSubtasks();

    public HashMap<Integer, Epic> getEpics();

    public void deleteTasks();

    public void deleteSubtasks();

    public void deleteEpics();

    public Task getTaskById(int id);

    public Subtask getSubtaskById(int id);

    public Epic getEpicById(int id);

    public void add(Task task);

    public void add(Subtask subtask);

    public void add(Epic epic);

    public void update(Task task);

    public void update(Subtask subtask);

    public void update(Epic epic);

    public void deleteTaskById(int id);

    public void deleteSubtaskById(int id);

    public void deleteEpicById(int id);

    public List<Task> getHistory();

}
