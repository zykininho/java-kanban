public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, String status, int epicId) {
        super(name, description, status);
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
        return epicId;
    }

}
