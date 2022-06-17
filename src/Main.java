import Managers.TaskManager;
import Tasks.*;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("First task", "Test", "NEW");
        taskManager.add(task1);
        Task task2 = new Task("Second task", "Test", "NEW");
        taskManager.add(task2);
        Epic epic1 = new Epic("First epic", "Test");
        taskManager.add(epic1);
        Subtask subtask1 = new Subtask("First subtask", "Test", "NEW", 3);
        taskManager.add(subtask1);
        Subtask subtask2 = new Subtask("Second subtask", "Test", "NEW", 3);
        taskManager.add(subtask2);
        Epic epic2 = new Epic("Second epic", "Test");
        taskManager.add(epic2);
        Subtask subtask3 = new Subtask("Third subtask", "Test", "NEW", 6);
        taskManager.add(subtask3);
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getEpics());
        task1.setStatus("IN_PROGRESS");
        taskManager.update(task1);
        task2.setStatus("IN_PROGRESS");
        taskManager.update(task2);
        subtask1.setStatus("DONE");
        taskManager.update(subtask1);
        subtask2.setStatus("DONE");
        taskManager.update(subtask2);
        subtask3.setStatus("IN_PROGRESS");
        taskManager.update(subtask3);
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getEpics());
        taskManager.deleteTaskById(task2.getId());
        taskManager.deleteEpicById(epic2.getId());
    }
}
