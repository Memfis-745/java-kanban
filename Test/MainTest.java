import static org.junit.jupiter.api.Assertions.*;

import managers.InMemoryTaskManager;
import managers.Managers;
import org.junit.jupiter.api.Test;
import tasks.Task;

class MainTest {
    static final InMemoryTaskManager taskManager = (InMemoryTaskManager) Managers.getDefault();
    Task task = new Task("Test addNewTask", "Test addNewTask description");
    @Test
    void taskWithSameIdEquals() { // проверка на равенство двух экземпляров задач с одинаковым id
        Task task1 = new Task("Test addNewTask", "Test addNewTask description");
        task1.setHandId(1);
        taskManager.addTask(task1);

        final Task savedTask = taskManager.showTask(1);
        final Task savedTaskCopy = taskManager.showTask(1);

        // Проверка на равенство двух задач с одинаковыми полями, но разными id
        assertEquals(savedTask, savedTaskCopy, "Задачи не совпадают.");
    }
}