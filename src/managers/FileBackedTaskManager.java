package managers;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    public static File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            HashMap<Integer, Task> tasks = this.getTasks();
            HashMap<Integer, Subtask> subtasks = this.getSubtasks();
            HashMap<Integer, Epic> epics = this.getEpics();

            bufferedWriter.write("id,type,name,status,description,epic");
            for (Task task : tasks.values()) {
                String stringTask = CSVTaskFormat.toString(task);
                bufferedWriter.newLine();
                bufferedWriter.write(stringTask);
            }
            for (Subtask subtask : subtasks.values()) {
                String stringSubtask = CSVTaskFormat.toString(subtask);
                bufferedWriter.newLine();
                bufferedWriter.write(stringSubtask);
            }
            for (Epic epic : epics.values()) {
                String stringEpic = CSVTaskFormat.toString(epic);
                bufferedWriter.newLine();
                bufferedWriter.write(stringEpic);
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
            boolean isFileHeading = true;
            boolean isLineHistory = false;
            while (bufferedReader.ready()) {
                if (isFileHeading) {
                    isFileHeading = false;
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
                        } else if (subtasks.containsKey(id)) {
                            task = subtasks.get(id);
                        } else if (epics.containsKey(id)) {
                            task = epics.get(id);
                        }
                        inMemoryHistoryManager.add(task);
                    }
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
