package tasks;

import enums.Status;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private Status status = Status.NEW;
    private final List<Subtask> subtasks = new ArrayList<>();

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

    public List<Subtask> getSubtasks() {
        return this.subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void setActualStatus() {
        boolean statusNew = false;
        boolean statusDone = false;
        if (subtasks.size() == 0) {
            this.status = Status.NEW;
            return;
        }
        for (Subtask subtask : subtasks) {
            Status statusSubtask = subtask.getStatus();
            if (statusSubtask == Status.NEW) {
                statusNew = true;
            } else if (statusSubtask == Status.DONE) {
                statusDone = true;
            }
        }
        if (statusNew && !statusDone) {
            this.status = Status.NEW;
        } else if (!statusNew && statusDone) {
            this.status = Status.DONE;
        } else {
            this.status = Status.IN_PROGRESS;
        }
    }
}
