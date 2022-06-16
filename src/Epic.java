import java.util.ArrayList;
import java.util.Arrays;

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
            if (subtask.status.equals("NEW")) {
                subtasksStatusNew = true;
            } else if (subtask.status.equals("DONE")) {
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
