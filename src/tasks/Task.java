package tasks;

import enums.*;

public class Task {
    // Владимир, здесь было замечание про модификаторы доступа, у этого класса есть наследники Epic и Subtask,
    // поэтому был выбран модификатор protected
    protected String name;
    protected String description;
    protected int id;
    protected Status status;
    protected TaskType type;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.type = TaskType.TASK;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status + '\'' + '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return this.status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskType getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(TaskType taskType) {
        this.type = taskType;
    }
}
