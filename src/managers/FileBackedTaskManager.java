package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.util.Set;


import static tasks.Status.*;
import static tasks.TypeTask.EPIC;
import static tasks.TypeTask.TASK;
import static tasks.TypeTask.SUBTASK;


public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    public final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;

    }


    public void save() {

        try (BufferedWriter fileWriter = Files.newBufferedWriter(Paths.get(file.toURI()), StandardCharsets.UTF_8)) {

            fileWriter.write("id,type,name,status,description,epic,duration,startTime\n");
            for (Task t : listOfTask.values()) {
                fileWriter.write(toString(t));
            }
            for (Epic k : listOfEpic.values()) {
                fileWriter.write(toString(k));
            }
            // System.out.println("Тока 3.");
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
            String time = task.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            taskToSting = String.format("%d,%s,%s,%S,%s,%d,%s,%s,%n", task.getId(), task.getType(), task.name, task.getStatus(), task.description, ((Subtask) task).getEpicId(), task.getDuration(), time);
        } else if (task.getType() == EPIC) {
            taskToSting = String.format("%d,%s,%s,%S,%s,%n", task.getId(), task.getType(), task.name, task.getStatus(), task.description);
        } else if (task.getType().equals(TASK)) {
            String time = task.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            taskToSting = String.format("%d,%s,%s,%S,%s,%s,%s,%n", task.getId(), task.getType(), task.name, task.getStatus(), task.description, task.getDuration(), time);
        }
        return taskToSting;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        List<String> strings;

        try {
            strings = Files.readAllLines(Paths.get(file.toURI()), StandardCharsets.UTF_8);


            for (String string : strings) {
                String[] newSplit = string.split(",");

                if (newSplit[1].equals(TASK.toString())) {
                    fileBackedTaskManager.listOfTask.put(Integer.parseInt(newSplit[0]), fromStringValue(string));
                    fileBackedTaskManager.validationTreeSet(fromStringValue(string));
                } else if (newSplit[1].equals(EPIC.toString())) {
                    fileBackedTaskManager.listOfEpic.put(Integer.parseInt(newSplit[0]), (Epic) fromStringValue(string));
                } else if (newSplit[1].equals(SUBTASK.toString())) {
                    fileBackedTaskManager.listOfSubTask.put(Integer.parseInt(newSplit[0]), (Subtask) fromStringValue(string));
                    fileBackedTaskManager.validationTreeSet(fromStringValue(string));
                    Epic epic = fileBackedTaskManager.listOfEpic.get(Integer.parseInt(newSplit[5]));
                    epic.setSubId(Integer.parseInt(newSplit[0]));
                    fileBackedTaskManager.epicSubTime(epic, (Subtask) fromStringValue(string));
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
                LocalDateTime startTime = LocalDateTime.parse(newSplit[6], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                task = new Task(newSplit[2], newSplit[4], Duration.parse(newSplit[5]), startTime);
                task.setId(Integer.parseInt(newSplit[0]));
                task.setStatus(valueOf(newSplit[3]));
                return task;

            case "EPIC":
                epic = new Epic(newSplit[2], newSplit[4]);
                epic.setId(Integer.parseInt(newSplit[0]));
                epic.setStatus(valueOf(newSplit[3]));
                return epic;
            case "SUBTASK":
                startTime = LocalDateTime.parse(newSplit[7], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                subtask = new Subtask(newSplit[2], newSplit[4], Integer.parseInt(newSplit[5]), Duration.parse(newSplit[6]), startTime);
                subtask.setId(Integer.parseInt(newSplit[0]));
                subtask.setStatus(valueOf(newSplit[3]));

                return subtask;
            default:
                break;
        }
        return null;
    }

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
    public Set<Task> getPrioritizedTasks() {
        save();
        return super.getPrioritizedTasks();
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

    @Override
    public void epicSubTime(Epic epic, Subtask subtask) {
        super.epicSubTime(epic, subtask);
        save();
    }

}