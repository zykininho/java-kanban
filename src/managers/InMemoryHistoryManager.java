package managers;

import tasks.Task;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final LinkedList<Task> taskViewHistory = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (taskViewHistory.size() == 10) {
            taskViewHistory.removeFirst();
        }
        taskViewHistory.add(task);
    }

    @Override
    public void remove(Task task) {
        if (taskViewHistory.contains(task)) {
            taskViewHistory.remove(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return taskViewHistory;
    }
}
