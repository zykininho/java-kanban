package managers;

import tasks.Task;
import java.util.List;

public interface HistoryManager {
    public void add(Task task);

    public void remove(Task task);

    public List<Task> getHistory();
}
