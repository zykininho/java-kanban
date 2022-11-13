package managers;

import java.io.File;

public final class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
//      return new InMemoryTaskManager();
        return new FileBackedTaskManager(new File("tasks.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
