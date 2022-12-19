package tasks;

import enums.Status;
import enums.TaskType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Subtask> subtasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskType.EPIC;
    }

    public Epic(String name, String description, long duration, LocalDateTime startTime) {
        super(name, description, duration, startTime);
        this.type = TaskType.EPIC;
    }

    public Epic(String name, String description, int id, Status status, long duration, LocalDateTime startTime, LocalDateTime endTime) {
        super(name, description, id, status, duration, startTime);
        this.type = TaskType.EPIC;
        this.endTime = endTime;
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

    public void setTime() {
        setDuration();
        setStartTime();
        setEndTime();
    }

    public void setDuration() {
        long duration = 0;
        for (Subtask subtask : subtasks) {
            duration += subtask.getDuration();
        }
        this.duration = duration;
    }

    public void setStartTime() {
        LocalDateTime minStartTime = null;
        for (Subtask subtask : subtasks) {
            LocalDateTime startTimeSubtask = subtask.getStartTime();
            if (minStartTime == null) {
                minStartTime = startTimeSubtask;
                continue;
            }
            if (startTimeSubtask.isBefore(minStartTime)) {
                minStartTime = startTimeSubtask;
            }
        }
        this.startTime = minStartTime;
    }

    public void setEndTime() {
        LocalDateTime maxEndTime = null;
        for (Subtask subtask : subtasks) {
            LocalDateTime endTimeSubtask = subtask.getEndTime();
            if (maxEndTime == null) {
                maxEndTime = endTimeSubtask;
                continue;
            }
            if (endTimeSubtask.isAfter(maxEndTime)) {
                maxEndTime = endTimeSubtask;
            }
        }
        this.endTime = maxEndTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return super.getEndTime();
    }
}
