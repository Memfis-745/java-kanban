package tasks;
import java.util.Objects;
import managers.*;

public class Task {
    public String name;
    public String description;
    public int id;
    public Integer handId;
    public Status taskStatus;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHandId(Integer handId) {
        this.handId = handId;
    }

    public void setStatus(Status status) {
        this.taskStatus = status;
    }
    public Integer getId() {
        if (handId == null) {
            return id;
        } else {
            return handId;
        }
    }
    public Status getStatus() {
        return taskStatus;
    }
    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", taskStatus=" + taskStatus +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && Objects.equals(taskStatus, task.getStatus());
    }
}
