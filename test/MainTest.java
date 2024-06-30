import managers.FileBackedTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void main() throws IOException {
        final File file = new File("taskFile.csv");
        final FileBackedTaskManager fileManagerOut = new FileBackedTaskManager(file);
        fileManagerOut.addTask(new Task("Задача-1", "Описание задачи-1"));
        fileManagerOut.addTask(new Task("Задача-2", "Описание задачи-2"));
        Integer epicId1 = fileManagerOut.addEpic(new Epic("эпик-3", "описание эпика-3"));

        FileBackedTaskManager fileManagerIn = FileBackedTaskManager.loadFromFile(file);
        assertEquals(fileManagerOut.getAllTask().size(), fileManagerIn.getAllTask().size(), "Количество задач не совпадает.");
        assertEquals(fileManagerOut.getAllTask().size(), fileManagerIn.getAllTask().size(), "Количество эпиков не совпадает.");

        assertEquals(fileManagerOut.showTask(1), fileManagerIn.showTask(1), "Задача не совпадает.");

    }
}

