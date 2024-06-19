package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;


import static tasks.Status.*;
import static tasks.TypeTask.EPIC;
import static tasks.TypeTask.TASK;
import static tasks.TypeTask.SUBTASK;


public class FileBackedTaskManager extends InMemoryTaskManager {
    public final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;

    }


    public void save() {

        try (BufferedWriter fileWriter = Files.newBufferedWriter(Paths.get(file.toURI()), StandardCharsets.UTF_8)) {

            fileWriter.write("id,type,name,status,description,epic\n");

            for (Task t : listOfTask.values()) {
                fileWriter.write(toString(t));
            }
            for (Epic k : listOfEpic.values()) {
                fileWriter.write(toString(k));
            }
            for (Subtask p : listOfSubTask.values()) {
                fileWriter.write(toString(p));
            }
        } catch (IOException e) {
            System.out.println("Ошибка сохранения данных.");
        }
    }

    public String toString(Task task) {
        String taskToSting = null;
        if (task.getType() == SUBTASK) {
            taskToSting = String.format("%d,%s,%s,%S,%s,%d,%n", task.getId(), task.getType(), task.name, task.getStatus(), task.description, ((Subtask) task).getEpicId());
        } else if (task.getType() == EPIC) {
            taskToSting = String.format("%d,%s,%s,%S,%s,%n", task.getId(), task.getType(), task.name, task.getStatus(), task.description);
        } else if (task.getType().equals(TASK)) {
            taskToSting = String.format("%d,%s,%s,%S,%s,%n", task.getId(), task.getType(), task.name, task.getStatus(), task.description);
        }
        return taskToSting;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        List<String> strings;

        try {
            strings = Files.readAllLines(Paths.get("taskFile.csv"), StandardCharsets.UTF_8);


            for (String string : strings) {
                String[] newSplit = string.split(",");

                if (newSplit[1].equals("TASK")) {
                    fileBackedTaskManager.listOfTask.put(Integer.parseInt(newSplit[0]), fromStringValue(string));
                } else if (newSplit[1].equals("EPIC")) {
                    fileBackedTaskManager.listOfEpic.put(Integer.parseInt(newSplit[0]), (Epic) fromStringValue(string));
                } else if (newSplit[1].equals("SUBTASK")) {
                    fileBackedTaskManager.listOfSubTask.put(Integer.parseInt(newSplit[0]), (Subtask) fromStringValue(string));
                    Epic epic = fileBackedTaskManager.listOfEpic.get(Integer.parseInt(newSplit[5]));
                    epic.setSubId(Integer.parseInt(newSplit[0]));
                }
            }
            return fileBackedTaskManager;
        } catch (IOException e) {
            System.out.println("Ошибка восстановления данных.");
        }
        return null;
    }


    public static Task fromStringValue(String value) {
        String[] newSplit = value.split(",");
        Task task;
        Epic epic;
        Subtask subtask;

        switch (newSplit[1]) {
            case "TASK":
                task = new Task(newSplit[2], newSplit[4]);
                task.setId(Integer.parseInt(newSplit[0]));
                task.setStatus(statik(newSplit[3]));
                return task;

            case "EPIC":
                epic = new Epic(newSplit[2], newSplit[4]);
                epic.setId(Integer.parseInt(newSplit[0]));
                epic.setStatus(statik(newSplit[3]));
                return epic;
            case "SUBTASK":
                subtask = new Subtask(newSplit[2], newSplit[4], Integer.parseInt(newSplit[5]));
                subtask.setId(Integer.parseInt(newSplit[0]));
                subtask.setStatus(statik(newSplit[3]));
                return subtask;
            default:
                break;
        }
        return null;
    }

    public static Status statik(String str) {
        switch (str) {
            case "NEW":
                return NEW;
            case "IN_PROGRESS":
                return IN_PROGRESS;
            case "DONE":
                return DONE;
            default:
                break;
        }
        return null;
    }


    //
    @Override
    public ArrayList<Task> getAllTask() {
        save();
        return super.getAllTask();
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        save();
        return super.getAllEpic();
    }

    @Override
    public ArrayList<Subtask> getAllSubTask() {
        save();
        return super.getAllSubTask();
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    @Override
    public void removeAllSubTask() {
        super.removeAllSubTask();
        save();
    }

    @Override
    public Task showTask(int id) {
        save();
        return super.showTask(id);
    }

    @Override
    public Epic showEpic(int id) {
        save();
        return super.showEpic(id);
    }

    @Override
    public Subtask showSubTask(int id) {
        save();
        return super.showSubTask(id);
    }

    @Override
    public int addTask(Task task) {
        save();
        return super.addTask(task);
    }

    @Override
    public Integer addEpic(Epic epic) {
        save();
        return super.addEpic(epic);
    }

    @Override
    public int addSubTask(Subtask subtask) {
        save();
        return super.addSubTask(subtask);
    }

    @Override
    public void updateTask(int id, String status) {
        super.updateTask(id, status);
        save();
    }

    @Override
    public void updateSubTask(int id, String status) {
        super.updateSubTask(id, status);
        save();
    }

    @Override
    public void removeByIdTask(int id) {
        super.removeByIdTask(id);
        save();
    }

    @Override
    public void removeByIdEpic(int id) {
        super.removeByIdEpic(id);
        save();
    }

    @Override
    public void removeByIdSubtask(int id) {
        super.removeByIdSubtask(id);
        save();
    }

    @Override
    public ArrayList<Subtask> showSubtask(int id) {
        save();
        return super.showSubtask(id);
    }

    @Override
    public List<Task> getHistory() {
        save();
        return super.getHistory();

    }

    @Override
    public void remove(int id) {
        super.remove(id);
        save();
    }

}

