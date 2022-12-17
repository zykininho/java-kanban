package managers;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public File file;

    public FileBackedTaskManager() {
        this.file = null;
    }

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        System.out.println("*** Создадим первый менеджер FileBackedTaskManager ***");
        FileBackedTaskManager fileBackedTaskManagerFirst = new FileBackedTaskManager(new File("tasks.csv"));
        System.out.println();
        System.out.println("*** Создадим задачи и добавим их в первый менеджер FileBackedTaskManager ***");
        Task task1 = new Task("First task", "Sprint 6 Task 1");
        fileBackedTaskManagerFirst.add(task1);
        Task task2 = new Task("Second task", "Sprint 6 Task 2");
        fileBackedTaskManagerFirst.add(task2);
        Epic epic1 = new Epic("First epic", "Sprint 6 Epic 1");
        fileBackedTaskManagerFirst.add(epic1);
        Epic epic2 = new Epic("Second epic", "Sprint 6 Epic 2");
        fileBackedTaskManagerFirst.add(epic2);
        Subtask subtask1 = new Subtask("First subtask", "Sprint 6 Subtask 1", 3);
        fileBackedTaskManagerFirst.add(subtask1);
        Subtask subtask2 = new Subtask("Second subtask", "Sprint 6 Subtask 2", 3);
        fileBackedTaskManagerFirst.add(subtask2);
        Subtask subtask3 = new Subtask("Third subtask", "Sprint 6 Subtask 3", 4);
        fileBackedTaskManagerFirst.add(subtask3);
        System.out.println();
        System.out.println("*** Выведем историю просмотров ***");
        fileBackedTaskManagerFirst.getTaskById(2);
        fileBackedTaskManagerFirst.getEpicById(3);
        fileBackedTaskManagerFirst.getSubtaskById(7);
        System.out.println(fileBackedTaskManagerFirst.getHistory());
        System.out.println();
        System.out.println("*** Создадим второй менеджер FileBackedTaskManager ***");
        FileBackedTaskManager fileBackedTaskManagerSecond = loadFromFile(new File("tasks.csv"));
        System.out.println();
        System.out.println("*** Выведем историю просмотров ***");
        System.out.println(fileBackedTaskManagerSecond.getHistory());
        System.out.println();
        System.out.println("*** Выведем задачи менеджера ***");
        System.out.println(fileBackedTaskManagerSecond.getTasks());
        System.out.println(fileBackedTaskManagerSecond.getEpics());
        System.out.println(fileBackedTaskManagerSecond.getSubtasks());
    }

    protected void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            HashMap<Integer, Task> tasks = this.getTasks();
            HashMap<Integer, Subtask> subtasks = this.getSubtasks();
            HashMap<Integer, Epic> epics = this.getEpics();

            bufferedWriter.write("id,type,name,status,description,epic,duration,startTime");
            for (Task task : tasks.values()) {
                String stringTask = CSVTaskFormat.toString(task);
                bufferedWriter.newLine();
                bufferedWriter.write(stringTask);
            }
            for (Epic epic : epics.values()) {
                String stringEpic = CSVTaskFormat.toString(epic);
                bufferedWriter.newLine();
                bufferedWriter.write(stringEpic);
            }
            for (Subtask subtask : subtasks.values()) {
                String stringSubtask = CSVTaskFormat.toString(subtask);
                bufferedWriter.newLine();
                bufferedWriter.write(stringSubtask);
            }
            bufferedWriter.newLine();
            String stringHistory = CSVTaskFormat.historyToString(this.getInMemoryHistoryManager());
            bufferedWriter.newLine();
            bufferedWriter.write(stringHistory);
        } catch (IOException e) {
            throw new ManagerSaveException("Не найден файл для сохранения списка и истории задач");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            boolean isLineHeading = true;
            boolean isLineHistory = false;
            while (bufferedReader.ready()) {
                if (isLineHeading) {
                    bufferedReader.readLine();
                    isLineHeading = false;
                    continue;
                }
                String stringLine = bufferedReader.readLine();
                if (stringLine.isEmpty()) {
                    isLineHistory = true;
                    continue;
                }
                if (isLineHistory) {
                    HistoryManager inMemoryHistoryManager = fileBackedTaskManager.getInMemoryHistoryManager();
                    List<Integer> listTasks = CSVTaskFormat.historyFromString(stringLine);
                    HashMap<Integer, Task> tasks = fileBackedTaskManager.getTasks();
                    HashMap<Integer, Subtask> subtasks = fileBackedTaskManager.getSubtasks();
                    HashMap<Integer, Epic> epics = fileBackedTaskManager.getEpics();
                    Task task = null;
                    for (Integer id : listTasks) {
                        if (tasks.containsKey(id)) {
                            task = tasks.get(id);
                        } else if (epics.containsKey(id)) {
                            task = epics.get(id);
                        } else if (subtasks.containsKey(id)) {
                            task = subtasks.get(id);
                        }
                        inMemoryHistoryManager.add(task);
                    }
                    continue;
                }
                Task task = CSVTaskFormat.fromString(stringLine);
                fileBackedTaskManager.addFromFile(task);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileBackedTaskManager;
    }

    @Override
    public void add(Subtask subtask) {
        super.add(subtask);
        save();
    }

    @Override
    public void add(Task task) {
        super.add(task);
        save();
    }

    @Override
    public void add(Epic epic) {
        super.add(epic);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    @Override
    public void update(Subtask subtask) {
        super.update(subtask);
        save();
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void addToHistory(Task task) {
        super.addToHistory(task);
        save();
    }

    @Override
    public void removeFromHistory(Task task) {
        super.removeFromHistory(task);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }
}
