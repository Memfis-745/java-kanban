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
//import org.apache.commons.text.WordUtils;


public class FileBackedTaskManager extends InMemoryTaskManager {
    File file;

    public FileBackedTaskManager(File file) {
        this.file = file;

    }


    public void save() throws IOException {

        try (BufferedWriter fileWriter = Files.newBufferedWriter(Paths.get(file.toURI()), StandardCharsets.UTF_8)) {

            fileWriter.write("id,type,name,status,description,epic\n");

            for (Task t : listOfTask.values()) {
                fileWriter.write(toString(t));

            }
            for (Epic k : listOfEpic.values()) {
                fileWriter.write(toString(k));
                //  System.out.println(li);
            }

            for (Subtask p : listOfSubTask.values()) {
                fileWriter.write(toString(p));

            }
        }
    }

    public String toString(Task task) {
        String taskToSting = null;
        if (task instanceof Subtask) {
            taskToSting = new String(task.getId() + ",SUBTASK," + task.name + "," + task.getStatus() + "," + task.description + "," + ((Subtask) task).getEpicId() + "\n");
        } else if (task instanceof Epic) {
            taskToSting = new String(task.getId() + ",EPIC," + task.name + "," + task.getStatus() + "," + task.description + "\n");
        } else if (task instanceof Task) {
            taskToSting = new String(task.getId() + ",TASK," + task.name + "," + task.getStatus() + "," + task.description + "\n");
        }
        return taskToSting;
    }

    // public void readFile(File file)  {
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        List<String> strings = null;

        try {
            strings = Files.readAllLines(Paths.get("taskFile.csv"), StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String string : strings) {
            String[] newSplit = string.split(",");

            if (newSplit[1].equals("TASK")) {

                fileBackedTaskManager.listOfTask.put(Integer.parseInt(newSplit[0]), fromStringValue(string));
            } else if (newSplit[1].equals("EPIC")) {
                fileBackedTaskManager.listOfEpic.put(Integer.parseInt(newSplit[0]), (Epic) fromStringValue(string));
            } else if (newSplit[1].equals("SUBTASK")) {
                // System.out.println("Из стрингов в массив  " +newSplit[0]+newSplit[1]+newSplit[2]+newSplit[3]+newSplit[4]+newSplit[5]);
                fileBackedTaskManager.listOfSubTask.put(Integer.parseInt(newSplit[0]), (Subtask) fromStringValue(string));
                Epic epic = fileBackedTaskManager.listOfEpic.get(Integer.parseInt(newSplit[5]));
                epic.setSubId(Integer.parseInt(newSplit[0]));
            }
            //System.out.println(string);
        }
        return fileBackedTaskManager;
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
                //  System.out.println("Таск освобожденный:" +task);
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
    public ArrayList<Task> getAllTask() throws IOException {
        save();
        return super.getAllTask();
    }

    @Override
    public ArrayList<Epic> getAllEpic() throws IOException {
        save();
        return super.getAllEpic();
    }

    @Override
    public ArrayList<Subtask> getAllSubTask() throws IOException {
        save();
        return super.getAllSubTask();
    }

    @Override
    public void removeAllTask() throws IOException {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeAllEpic() throws IOException {
        super.removeAllEpic();
        save();
    }

    @Override
    public void removeAllSubTask() throws IOException {
        super.removeAllSubTask();
        save();
    }

    @Override
    public Task showTask(int id) throws IOException {
        save();
        return super.showTask(id);
    }

    @Override
    public Epic showEpic(int id) throws IOException {
        save();
        return super.showEpic(id);
    }

    @Override
    public Subtask showSubTask(int id) throws IOException {
        save();
        return super.showSubTask(id);
    }

    @Override
    public int addTask(Task task) throws IOException {
        save();
        return super.addTask(task);
    }

    @Override
    public Integer addEpic(Epic epic) throws IOException {
        save();
        return super.addEpic(epic);
    }

    @Override
    public int addSubTask(Subtask subtask) throws IOException {
        save();
        return super.addSubTask(subtask);
    }

    @Override
    public void updateTask(int id, String status) throws IOException {
        super.updateTask(id, status);
        save();
    }

    @Override
    public void updateSubTask(int id, String status) throws IOException {
        super.updateSubTask(id, status);
        save();
    }

    @Override
    public void removeByIdTask(int id) throws IOException {
        super.removeByIdTask(id);
        save();
    }

    @Override
    public void removeByIdEpic(int id) throws IOException {
        super.removeByIdEpic(id);
        save();
    }

    @Override
    public void removeByIdSubtask(int id) throws IOException {
        super.removeByIdSubtask(id);
        save();
    }

    @Override
    public ArrayList<Subtask> showSubtask(int id) throws IOException {
        save();
        return super.showSubtask(id);
    }

    @Override
    public List<Task> getHistory() throws IOException {
        save();
        return super.getHistory();

    }

    @Override
    public void remove(int id) throws IOException {
        super.remove(id);
        save();
    }

}


 /* public void save(){
        BufferedWriter writer;
        writer = Files.newBufferedWriter(Paths.get(outPutFile.toURI()), StandardCharsets.UTF_8);
    }
    private static void writeToImage() throws IOException {
        try (OutputStream outputStream = new FileOutputStream("read-image-copy.txt", true)) {
            outputStream.write("This is a line from the webinar!".getBytes(StandardCharsets.UTF_8));
        }
    }
    private static void writeToTextFile() throws IOException {
        Random random = new Random();
        try (Writer writer = new BufferedWriter(new FileWriter("write-text.txt"))) {
            for (int i = 0; i < 100; i++) {
                writer.write(random.nextInt());
            }
taskManager.addTask(new Task("Задача-1", "Описание задачи-1"));
        taskManager.addTask(new Task("Задача-2", "Описание задачи-2"));
        Integer epicId1 = taskManager.addEpic(new Epic("эпик-3", "описание эпика-3"));
        Integer epicId2 = taskManager.addEpic(new Epic("эпик-4", "описание эпика-4"));
        taskManager.addSubTask(new Subtask("подзадача-5", "первая подзадача к эпику 3", epicId1));
        taskManager.addSubTask(new Subtask("подзадача-6", "вторая подзадача к эпику 3", epicId1));
        taskManager.addSubTask(new Subtask("подзадача-7", "первая подзадача к эпику 4", epicId2));



        id,type,name,status,description,epic
        1,TASK,Task1,NEW,Description task1,
                2,EPIC,Epic2,DONE,Description epic2,
                3,SUBTASK,Sub Task2,DONE,Description sub task3,2
        tasknewSplit[0]=capitalize(newSplit[0]);
        newSplit[1]=capitalize(newSplit[1]);
        newSplit[2]=newSplit[2].toLowerCase();
        // scoolnik = String.join(" ", newSplit);
        i++;
        scoolnik =  newSplit[0]+" "+newSplit[1]+" "+newSplit[2]+" - " + gradeToString(newSplit[3]);
        System.out.println(scoolnik);
    }
    }

*/