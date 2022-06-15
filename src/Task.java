public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected String status; // NEW, IN_PROGRESS, DONE

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
//        this.id = taskManager.getId();
        this.status = status;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
