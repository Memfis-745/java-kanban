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
        assertEquals(fileManagerOut.getAllTask().size() , fileManagerIn.getAllTask().size(), "Количество задач не совпадает.");
        assertEquals(fileManagerOut.getAllTask().size() , fileManagerIn.getAllTask().size(), "Количество эпиков не совпадает.");

        assertEquals(fileManagerOut.showTask(1) , fileManagerIn.showTask(1), "Задача не совпадает.");

    }
}


/*f (fileManagerOut.getAllEpic().size() != fileManagerIn.getAllEpic().size()) {
            System.out.println("Количество эпиков не совпадает");
        }
        if (fileManagerOut.getAllSubTask().size() != fileManagerIn.getAllSubTask().size()) {
            System.out.println("Количество эпиков не совпадает");
        }


        final Task savedTask = taskManager.showTask(1);
        final Task savedTaskCopy = taskManager.showTask(1);

        // Проверка на равенство двух задач с одинаковыми полями, но разными id
        assertEquals(savedTask, savedTaskCopy, "Задачи не совпадают.");*/