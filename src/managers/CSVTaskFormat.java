package managers;

import enums.*;
import tasks.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private static final String dateFormat = "yyyyMMddHHmmss";

    public static String toString(Task task) {
        String value = String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                            task.getId(),
                            task.getType(),
                            task.getName(),
                            task.getStatus(),
                            task.getDescription(),
                            getEpicIdIfSubtask(task),
                            task.getDuration(),
                            getTaskStartTime(task));
        return value;
    }

    private static String getEpicIdIfSubtask(Task task) {
        String epicId = "";
        TaskType taskType = task.getType();
        if (taskType == TaskType.SUBTASK) {
            Subtask subtask = (Subtask) task;
            epicId = String.valueOf(subtask.getEpicId());
            return epicId;
        }
        return epicId;
    }

    private static String getTaskStartTime(Task task) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
        LocalDateTime taskStartTime = task.getStartTime();
        String stringStartTime = (taskStartTime == null) ? ("") : dateTimeFormatter.format(task.getStartTime());
        return stringStartTime;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        long duration = Long.parseLong(taskProperties[6]);
        LocalDateTime startTime = LocalDateTime.parse(taskProperties[7], formatter);
        Task task = null;
        switch (taskType) {
            case EPIC:
                task = new Epic(name, description, duration, startTime);
                Epic epic = (Epic) task;
                epic.setTime();
                break;
            case TASK:
                task = new Task(name, description, duration, startTime);
                break;
            case SUBTASK:
                task = new Subtask(name, description, epicId, duration, startTime);
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
