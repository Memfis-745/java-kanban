package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import managers.*;

import tasks.TypeTask;

import tasks.TypeTask.*;

public class Task {
    public String name;
    public String description;
    public int id;
    public Integer handId;
    public Status taskStatus;
    protected Duration duration;
    public LocalDateTime startTime;
    protected LocalDateTime finishTime;


    public Task(String name, String description, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        getFinish();
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public LocalDateTime getStart() {
        return startTime;
    }

    public Duration getDuration() {

        return duration;
    }

    public LocalDateTime getFinish() {
        finishTime = startTime.plus(duration);
        return finishTime;
    }

    public TypeTask getType() {
        return TypeTask.TASK;
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
                ", start = " + startTime +
                ", duration= " + duration +
                ", finish = " + finishTime +
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
