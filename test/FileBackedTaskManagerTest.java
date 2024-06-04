import managers.FileBackedTaskManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import tasks.Status;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileBackedTaskManagerTest {
    int id;
    Task task = new Task("Test addNewTask", "Test addNewTask description");
    Status status = task.getStatus();

    @Test
    void writeEmptyFile() throws IOException {
        //  final File file = new File("taskFile.csv");
        File file = File.createTempFile("taskFileTest.csv", null);
        final FileBackedTaskManager fileManagerOut = new FileBackedTaskManager(file);
        fileManagerOut.save();
        assertNotNull("taskFileTest.csv", "Файла нет");
        if (file.exists()) {
            System.out.println("Пустой файл создан: " + file.getName());
        } else {
            System.out.println("Пустой файл не создан: " + file.getName());
        }

    }
/*
    @Test
    void testToString() {
    }*/

    @Test
    void loadFromEmptyFile() throws IOException {
        File file = File.createTempFile("taskFileTest.csv", null);
        final FileBackedTaskManager fileManagerOut = new FileBackedTaskManager(file);

        List<String> strings = null;
        strings = Files.readAllLines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);
        System.out.println("Чтение из пустого файла: " + strings);

    }
}




   /* @Test

    void loadFromString() throws IOException {
        File file = File.createTempFile("taskFileTest.csv", null);
        final FileBackedTaskManager fileManagerOut = new FileBackedTaskManager(file);

        fileManagerOut.addTask(new Task("Задача-1", "Описание задачи-1"))

        String st = fileManagerOut.toString();

        List<String> strings = null;
        strings = Files.readAllLines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);
        strings[1];
        System.out.println("Чтение из пустого файла: " + strings);
        if (strings == null) {
            System.out.println("Чтение из устого файла: " + file.getName());

    }}*/

/*
    @Test
    void statik() {
    }

    @Test
    void getAllTask() {
    }

    @Test
    void getAllEpic() {
    }

    @Test
    void getAllSubTask() {
    }

    @Test
    void removeAllTask() {
    }

    @Test
    void removeAllEpic() {
    }

    @Test
    void removeAllSubTask() {
    }

    @Test
    void showTask() {
    }

    @Test
    void showEpic() {
    }

    @Test
    void showSubTask() {
    }

    @Test
    void addTask() {
    }

    @Test
    void addEpic() {
    }

    @Test
    void addSubTask() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void updateSubTask() {
    }

    @Test
    void removeByIdTask() {
    }

    @Test
    void removeByIdEpic() {
    }

    @Test
    void removeByIdSubtask() {
    }

    @Test
    void showSubtask() {
    }

    @Test
    void getHistory() {
    }

    @Test
    void remove() {

}

} */