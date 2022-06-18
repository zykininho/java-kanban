package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private String status = "NEW";
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
            this.status = "NEW";
            return;
        }
        for (Subtask subtask : subtasks) {
            String statusSubtask = subtask.getStatus();
            if (statusSubtask.equals("NEW")) {
                subtasksStatusNew = true;
            } else if (statusSubtask.equals("DONE")) {
                subtasksStatusDone = true;
            }
        }
        if (subtasksStatusNew && !subtasksStatusDone) {
            this.status = "NEW";
        } else if (!subtasksStatusNew && subtasksStatusDone) {
            this.status = "DONE";
        } else {
            this.status = "IN_PROGRESS";
        }
    }
}
