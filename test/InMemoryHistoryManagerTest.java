import org.junit.jupiter.api.Test;
//import tasks.Status;
import tasks.*;
import managers.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    static final InMemoryTaskManager taskManager = (InMemoryTaskManager) Managers.getDefault();
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    void add() throws IOException { // Проверка, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
        Task task = new Task("Задача-1", "Описание задачи-1", Duration.ofMinutes(120), LocalDateTime.of(2024, 1, 1, 0, 0));
        task.setStatus(Status.NEW); // тестовый коммент

        taskManager.addTask(task);

        historyManager.addHistory(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История пустая.");
        assertEquals(task, history.get(0), "Задача не соответствует оригиналу.");
    }

    @Test
    void remove() throws IOException { // Проверка, удаления истории по id
        Task task = new Task("Задача-1", "Описание задачи-1", Duration.ofMinutes(120), LocalDateTime.of(2024, 1, 1, 0, 0));
        Task task2 = new Task("Задача-2", "Описание задачи-2", Duration.ofMinutes(120), LocalDateTime.of(2024, 2, 1, 0, 0));
        Task task3 = new Task("Задача-3", "Описание задачи-3", Duration.ofMinutes(120), LocalDateTime.of(2024, 3, 1, 0, 0));
        task.setStatus(Status.NEW);
        task2.setStatus(Status.NEW);
        task3.setStatus(Status.NEW);
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        historyManager.addHistory(task);
        historyManager.addHistory(task2);
        historyManager.addHistory(task3);
        final List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size(), "История пустая.");
        assertEquals(task, history.get(0), "Задача не соответствует оригиналу.");
        assertEquals(task2, history.get(1), "Задача не соответствует оригиналу.");
        assertEquals(task3, history.get(2), "Задача не соответствует оригиналу.");
        historyManager.remove(2);
        assertEquals(2, history.size(), "История пустая.");
        assertEquals(task, history.get(0), "Задача не соответствует оригиналу.");
        assertEquals(task3, history.get(1), "Задача не соответствует оригиналу.");
    }
}
