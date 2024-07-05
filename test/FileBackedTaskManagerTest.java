import managers.FileBackedTaskManager;
import managers.InMemoryTaskManager;
import managers.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import tasks.Epic;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>
class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    //  static final FileBackedTaskManager taskManager = (FileBackedTaskManager) Managers.getDefault();
    int id;
    Task task = new Task("Test addNewTask", "Test addNewTask description");
    Status status = task.getStatus();
    //Path somePath = Paths.get("test.csv");
    final File file = new File("taskFile.csv");

    @Override
    void initial() {
        taskManager = new FileBackedTaskManager(file);
    }


    @BeforeEach
    void beforeEach() {
        initial();
    }

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


        //  final File file = new File("taskFileTest.csv");
        //File file = File.createTempFile("taskFileTest.csv", null);
        //  final FileBackedTaskManager fileManagerOut = new FileBackedTaskManager(file);

       /* Epic epic = new Epic("эпик-3", "описание эпика-3");
        Integer epicId = fileManagerOut.addEpic(epic);
        fileManagerOut.save();

        Subtask subtask = new Subtask("подзадача-5", "первая подзадача к эпику 3", epic.getId(), Duration.ofMinutes(103), LocalDateTime.of(2024, 1, 1, 0, 0));
        Subtask subtask1  = new Subtask("подзадача-6", "вторая подзадача к эпику 3", epic.getId(), Duration.ofMinutes(110), LocalDateTime.of(2024, 2, 1, 0, 0));
        fileManagerOut.addSubTask(subtask);
        fileManagerOut.save();
        fileManagerOut.addSubTask(subtask1);
        fileManagerOut.save();



        //int epicId = epic.getId();
        System.out.println(epicId);
        int subtaskId = subtask.getId();
        System.out.println(subtaskId);
        int subtaskId1 = subtask1.getId();
        System.out.println(subtaskId1);
        System.out.println(fileManagerOut.showEpic(epicId));
        System.out.println(fileManagerOut.showSubTask(subtaskId));
        System.out.println(fileManagerOut.showSubTask(subtaskId1));

        final FileBackedTaskManager fileManagerIn = FileBackedTaskManager.loadFromFile(file);
        // FileBackedTaskManager fileManagerIn = FileBackedTaskManager.loadFromFile(file);
        Epic epic2 = fileManagerIn.showEpic(epicId);
        System.out.println(epic2);
        System.out.println(fileManagerIn.showEpic(epicId));
        System.out.println(fileManagerIn.showSubTask(subtaskId));
        System.out.println(fileManagerIn.showEpic(epicId).getStatus());
        System.out.println(fileManagerIn.showSubTask(subtaskId).getStatus());


        assertEquals(fileManagerIn.showEpic(epicId).getStatus(),fileManagerIn.showSubTask(subtaskId).getStatus());
*/
    }


}