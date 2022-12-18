import enums.Status;
import http.HttpTaskServer;
import http.KVServer;
import managers.*;
import tasks.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        System.out.println("Сервер хранилища задач запущен");

        HttpTaskManager taskManager = new HttpTaskManager();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
        System.out.println("Сервер работы с задачами запущен");

        System.out.println();
        System.out.println("*** Создадим задачи разных типов ***");
        Task task1 = new Task("First task", "Test");
        taskManager.add(task1);
        Task task2 = new Task("Second task", "Test");
        taskManager.add(task2);
        Epic epic1 = new Epic("First epic", "Test");
        taskManager.add(epic1);
        Subtask subtask1 = new Subtask("First subtask", "Test", 3);
        taskManager.add(subtask1);
        Subtask subtask2 = new Subtask("Second subtask", "Test", 3);
        taskManager.add(subtask2);
        Subtask subtask3 = new Subtask("Third subtask", "Test", 3);
        taskManager.add(subtask3);
        Epic epic2 = new Epic("Second epic", "Test");
        taskManager.add(epic2);
        System.out.println("*** Задачи созданы ***");
        System.out.println();

        System.out.println("*** Выведем списки созданных задач ***");
        System.out.println("Список задач Tasks");
        System.out.println(taskManager.getTasks());
        System.out.println("Список задач Subtasks");
        System.out.println(taskManager.getSubtasks());
        System.out.println("Список задач Epics");
        System.out.println(taskManager.getEpics());
        System.out.println("*** Задачи отображены ***");
        System.out.println();

        System.out.println("*** Выведем историю просмотров ***");
        System.out.println(taskManager.getHistory());
        System.out.println();

        System.out.println("*** Ещё раз выведем списки созданных задач ***");
        System.out.println("Список задач Subtasks");
        System.out.println(taskManager.getSubtasks());
        System.out.println("Список задач Epics");
        System.out.println(taskManager.getEpics());
        System.out.println("Список задач Tasks");
        System.out.println(taskManager.getTasks());
        System.out.println("*** Задачи отображены ***");
        System.out.println();

        System.out.println("*** Выведем историю просмотров ***");
        System.out.println(taskManager.getHistory());
        System.out.println();

        System.out.println("Установим новые статусы задачам");
        task1.setStatus(Status.IN_PROGRESS);
        taskManager.update(task1);
        task2.setStatus(Status.IN_PROGRESS);
        taskManager.update(task2);
        subtask1.setStatus(Status.DONE);
        taskManager.update(subtask1);
        subtask2.setStatus(Status.DONE);
        taskManager.update(subtask2);
        subtask3.setStatus(Status.IN_PROGRESS);
        taskManager.update(subtask3);
        System.out.println();

        System.out.println("*** Выведем историю просмотров ***");
        taskManager.getTaskById(1);
        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(5);
        System.out.println(taskManager.getHistory());
        System.out.println();

        System.out.println("*** Выведем историю просмотров ***");
        taskManager.getSubtaskById(6);
        taskManager.getEpicById(3);
        taskManager.getTaskById(2);
        System.out.println(taskManager.getHistory());
        System.out.println();

        System.out.println("*** Выведем списки задач с новыми статусами ***");
        System.out.println("Список задач Tasks:");
        System.out.println(taskManager.getTasks());
        System.out.println("Список задач Subtasks:");
        System.out.println(taskManager.getSubtasks());
        System.out.println("Список задач Epics:");
        System.out.println(taskManager.getEpics());
        System.out.println();

        System.out.println("*** Выведем историю просмотров ***");
        System.out.println(taskManager.getHistory());
        System.out.println();

        System.out.println("*** Удалим некоторые задачи ***");
        taskManager.deleteTaskById(task2.getId());
        System.out.println(taskManager.getHistory());
        System.out.println();

        System.out.println("*** Удалим некоторые задачи ***");
        taskManager.deleteEpicById(epic1.getId());
        System.out.println(taskManager.getHistory());
        System.out.println();

/*        kvServer.stop();
        System.out.println("Сервер хранилища задач остановлен");
        httpTaskServer.stop();
        System.out.println("Сервер работы с задачами остановлен");*/
    }
}
