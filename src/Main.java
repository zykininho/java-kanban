import enums.Status;
import http.KVServer;
import managers.*;
import tasks.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        KVServer server = new KVServer();
        try {
            server.start();
            System.out.println("Сервер хранилища задач запущен");

            TaskManager inMemoryTaskManager = Managers.getDefault();

            System.out.println("*** Создадим задачи разных типов ***");
            Task task1 = new Task("First task", "Test");
            inMemoryTaskManager.add(task1);
            Task task2 = new Task("Second task", "Test");
            inMemoryTaskManager.add(task2);
            Epic epic1 = new Epic("First epic", "Test");
            inMemoryTaskManager.add(epic1);
            Subtask subtask1 = new Subtask("First subtask", "Test", 3);
            inMemoryTaskManager.add(subtask1);
            Subtask subtask2 = new Subtask("Second subtask", "Test", 3);
            inMemoryTaskManager.add(subtask2);
            Subtask subtask3 = new Subtask("Third subtask", "Test", 3);
            inMemoryTaskManager.add(subtask3);
            Epic epic2 = new Epic("Second epic", "Test");
            inMemoryTaskManager.add(epic2);
            System.out.println("*** Задачи созданы ***");
            System.out.println();

            System.out.println("*** Выведем списки созданных задач ***");
            System.out.println("Список задач Tasks");
            System.out.println(inMemoryTaskManager.getTasks());
            System.out.println("Список задач Subtasks");
            System.out.println(inMemoryTaskManager.getSubtasks());
            System.out.println("Список задач Epics");
            System.out.println(inMemoryTaskManager.getEpics());
            System.out.println("*** Задачи отображены ***");
            System.out.println();

            System.out.println("*** Выведем историю просмотров ***");
            System.out.println(inMemoryTaskManager.getHistory());
            System.out.println();

            System.out.println("*** Ещё раз выведем списки созданных задач ***");
            System.out.println("Список задач Subtasks");
            System.out.println(inMemoryTaskManager.getSubtasks());
            System.out.println("Список задач Epics");
            System.out.println(inMemoryTaskManager.getEpics());
            System.out.println("Список задач Tasks");
            System.out.println(inMemoryTaskManager.getTasks());
            System.out.println("*** Задачи отображены ***");
            System.out.println();

            System.out.println("*** Выведем историю просмотров ***");
            System.out.println(inMemoryTaskManager.getHistory());
            System.out.println();

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
            System.out.println();

            System.out.println("*** Выведем историю просмотров ***");
            inMemoryTaskManager.getTaskById(1);
            inMemoryTaskManager.getSubtaskById(4);
            inMemoryTaskManager.getSubtaskById(5);
            System.out.println(inMemoryTaskManager.getHistory());
            System.out.println();

            System.out.println("*** Выведем историю просмотров ***");
            inMemoryTaskManager.getSubtaskById(6);
            inMemoryTaskManager.getEpicById(3);
            inMemoryTaskManager.getTaskById(2);
            System.out.println(inMemoryTaskManager.getHistory());
            System.out.println();

            System.out.println("*** Выведем списки задач с новыми статусами ***");
            System.out.println("Список задач Tasks:");
            System.out.println(inMemoryTaskManager.getTasks());
            System.out.println("Список задач Subtasks:");
            System.out.println(inMemoryTaskManager.getSubtasks());
            System.out.println("Список задач Epics:");
            System.out.println(inMemoryTaskManager.getEpics());
            System.out.println();

            System.out.println("*** Выведем историю просмотров ***");
            System.out.println(inMemoryTaskManager.getHistory());
            System.out.println();

            System.out.println("*** Удалим некоторые задачи ***");
            inMemoryTaskManager.deleteTaskById(task2.getId());
            System.out.println(inMemoryTaskManager.getHistory());
            System.out.println();

            System.out.println("*** Удалим некоторые задачи ***");
            inMemoryTaskManager.deleteEpicById(epic1.getId());
            System.out.println(inMemoryTaskManager.getHistory());
            System.out.println();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            server.stop();
            System.out.println("Сервер хранилища задач остановлен");
        }

    }
}
