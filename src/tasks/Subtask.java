package tasks;


import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    int epicId;

    public Subtask(String name, String description, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
    }

    public TypeTask getType() {
        return TypeTask.SUBTASK;
    }

    public Status getStatus() {
        return taskStatus;
    }

    public int getEpicId() {
        return epicId;
    }

    public boolean setSubId(int setSubId) {
        boolean epicSubMistake;
        if (setSubId != epicId) {
            id = setSubId;
            epicSubMistake = true;
        } else {
            System.out.println("Ошибка наследования эпика");
            epicSubMistake = false;
        }
        return epicSubMistake;
    }

    @Override
    public String toString() {

        return "Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", taskStatus=" + taskStatus + ", epicId " + epicId +
                ", start = " + startTime +
                ", duration= " + duration +
                ", finish = " + finishTime +
                '}';
    }
}
