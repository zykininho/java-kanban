package tasks;

import managers.Status;

import java.util.ArrayList;

public class Epic extends Task {
    private Status status = Status.NEW;
    private ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", subtasks='" + subtasks + '\'' + '}';
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void setActualStatus() {
        boolean subtasksStatusNew = false;
        boolean subtasksStatusDone = false;
        if (subtasks.size() == 0) {
            this.status = Status.NEW;
            return;
        }
        for (Subtask subtask : subtasks) {
            Status statusSubtask = subtask.getStatus();
            if (statusSubtask == Status.NEW) {
                subtasksStatusNew = true;
            } else if (statusSubtask == Status.DONE) {
                subtasksStatusDone = true;
            }
        }
        if (subtasksStatusNew && !subtasksStatusDone) {
            this.status = Status.NEW;
        } else if (!subtasksStatusNew && subtasksStatusDone) {
            this.status = Status.DONE;
        } else {
            this.status = Status.IN_PROGRESS;
        }
    }
}
