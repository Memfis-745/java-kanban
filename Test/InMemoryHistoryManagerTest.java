import org.junit.jupiter.api.Test;
//import tasks.Status;
import tasks.*;
import managers.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    static final InMemoryTaskManager taskManager = (InMemoryTaskManager) Managers.getDefault();
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    @Test
    void add() { // Проверка, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        task.setStatus(Status.NEW);
        taskManager.addTask(task);

        historyManager.addHistory(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История пустая.");
        assertEquals(task, history.get(0), "Задача не соответствует оригиналу.");


    }
}