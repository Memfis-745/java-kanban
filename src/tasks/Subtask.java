package tasks;

import managers.*;

public class Subtask extends Epic {
    int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
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
                ", taskStatus=" + taskStatus +
                '}';
    }
}
