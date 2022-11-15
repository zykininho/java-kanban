package managers;

import enums.*;
import tasks.*;

import java.util.ArrayList;
import java.util.List;

/* Сначала через запятую перечисляются все поля задач.
Ниже находится список задач, каждая из них записана с новой строки.
Дальше — пустая строка, которая отделяет задачи от истории просмотров.
И заключительная строка — это идентификаторы задач из истории просмотров. */

/*
id,type,name,status,description,epic
1,TASK,Task1,NEW,Description task1,
2,EPIC,Epic2,DONE,Description epic2,
3,SUBTASK,Sub Task2,DONE,Description sub task3,2

2,3
 */

public class CSVTaskFormat {

    public static String toString(Task task) {
        String value = String.format("%s,%s,%s,%s,%s",
                task.getId(),
                task.getType(),
                task.getName(),
                task.getStatus(),
                task.getDescription());
        TaskType taskType = task.getType();
        if (taskType == TaskType.SUBTASK) {
            Subtask subtask = (Subtask) task;
            String epicId = String.valueOf(subtask.getEpicId());
            value = value.concat(",").concat(epicId);
        }
        return value;
    }

    public static Task fromString(String value) {
        String[] taskProperties = value.split(",");
        int id = Integer.parseInt(taskProperties[0]);
        TaskType taskType = TaskType.valueOf(taskProperties[1]);
        String name = taskProperties[2];
        Status status = Status.valueOf(taskProperties[3]);
        String description = taskProperties[4];
        int epicId = 0;
        if (taskType == TaskType.SUBTASK) {
            epicId = Integer.parseInt(taskProperties[5]);
        }
        Task task = null;
        switch (taskType) {
            case EPIC:
                task = new Epic(name, description);
                break;
            case TASK:
                task = new Task(name, description);
                break;
            case SUBTASK:
                task = new Subtask(name, description, epicId);
                break;
        }
        task.setId(id);
        task.setStatus(status);
        return task;
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder value = new StringBuilder();
        List<Task> history = manager.getHistory();
        boolean firstTask = true;
        for (Task task : history) {
            if (firstTask) {
                value.append(task.getId());
                firstTask = false;
                continue;
            }
            value.append(",");
            value.append(task.getId());
        }
        return value.toString();
    }

    public static List<Integer> historyFromString(String value) {
        String[] listId = value.split(",");
        List<Integer> listTasks = new ArrayList<>();
        for (String id : listId) {
            listTasks.add(0, Integer.valueOf(id));
        }
        return listTasks;
    }
}
