import enums.Status;
import managers.*;
import tasks.*;

public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        System.out.println("Создадим задачи разных типов");
        Task task1 = new Task("First task", "Test", Status.NEW);
        inMemoryTaskManager.add(task1);
        Task task2 = new Task("Second task", "Test", Status.NEW);
        inMemoryTaskManager.add(task2);
        Epic epic1 = new Epic("First epic", "Test");
        inMemoryTaskManager.add(epic1);
        Subtask subtask1 = new Subtask("First subtask", "Test", Status.NEW, 3);
        inMemoryTaskManager.add(subtask1);
        Subtask subtask2 = new Subtask("Second subtask", "Test", Status.NEW, 3);
        inMemoryTaskManager.add(subtask2);
        Epic epic2 = new Epic("Second epic", "Test");
        inMemoryTaskManager.add(epic2);
        Subtask subtask3 = new Subtask("Third subtask", "Test", Status.NEW, 6);
        inMemoryTaskManager.add(subtask3);

        System.out.println("Выведем списки созданных задач");
        System.out.println("Список задач Tasks");
        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println("Список задач Subtasks");
        System.out.println(inMemoryTaskManager.getSubtasks());
        System.out.println("Список задач Epics");
        System.out.println(inMemoryTaskManager.getEpics());

        System.out.println("Установим новые статусы задачам");
        task1.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.update(task1);
        task2.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.update(task2);
        subtask1.setStatus(Status.DONE);
        inMemoryTaskManager.update(subtask1);
        subtask2.setStatus(Status.DONE);
        inMemoryTaskManager.update(subtask2);
        subtask3.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.update(subtask3);

        System.out.println("Выведем списки задач с новыми статусами");
        System.out.println("Список задач Tasks");
        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println("Список задач Subtasks");
        System.out.println(inMemoryTaskManager.getSubtasks());
        System.out.println("Список задач Epics");
        System.out.println(inMemoryTaskManager.getEpics());

        System.out.println("Удалим некоторые задачи");
        inMemoryTaskManager.deleteTaskById(task2.getId());
        inMemoryTaskManager.deleteEpicById(epic2.getId());

        System.out.println("Выведем историю просмотров");
        inMemoryTaskManager.getTaskById(1);
        System.out.println(inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getSubtaskById(4);
        System.out.println(inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getSubtaskById(5);
        System.out.println(inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getSubtaskById(7);
        System.out.println(inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getEpicById(3);
        System.out.println(inMemoryTaskManager.getHistory());
    }
}
