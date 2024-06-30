import managers.FileBackedTaskManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import tasks.Status;
import tasks.Task;
import tasks.Epic;

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

    @Test
    void loadFromEmptyFile() throws IOException {
        File file = File.createTempFile("taskFileTest.csv", null);
        final FileBackedTaskManager fileManagerOut = new FileBackedTaskManager(file);

        List<String> strings = null;
        strings = Files.readAllLines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);
        System.out.println("Чтение из пустого файла: " + strings);

    }


    @Test
    void fromTaskToStringValueAndBackAgain() throws IOException {

        File file = File.createTempFile("taskFileTest.csv", null);
        final FileBackedTaskManager fileManagerOut = new FileBackedTaskManager(file);
        Epic epic1 = new Epic("Задача-1", "Описание-1");
        epic1.setId(2);
        epic1.setStatus(Status.NEW);

        String stringToTask = fileManagerOut.toString(epic1);
        Epic epic2 = (Epic) fileManagerOut.fromStringValue(stringToTask);
        assertEquals(epic1, epic2, "Задачи не совпадают.");

    }

}