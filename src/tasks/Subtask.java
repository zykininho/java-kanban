package tasks;

import enums.Status;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", epicId='" + epicId + '\'' +
                ", status='" + status + '\'' + '}';
    }

    public int getEpicId() {
        return this.epicId;
    }
}
