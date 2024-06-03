
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import tasks.Status;
import tasks.*;
import managers.*;

import java.util.ArrayList;

class InMemoryTaskManagerTest {
    static final InMemoryTaskManager taskManager = (InMemoryTaskManager) Managers.getDefault();
    String name;
    String description;

    int id;
    Task task = new Task("Test addNewTask", "Test addNewTask description");
    Status status = task.getStatus();

    @BeforeEach
    void beforeEach() {
        taskManager.removeAllTask();
        taskManager.removeAllEpic();
        taskManager.removeAllSubTask();
    }

    @Test
        // проверка двух экземпляров задачи на равенство друг другу
    void taskEqualsHimself() {
        Task task1 = new Task("Test addNewTask", "Test addNewTask description");
        task1.setId(1);

        Task task2 = new Task("Test addNewTask", "Test addNewTask description");
        task2.setId(1);

        // Проверка на равенство двух задач с одинаковыми полями, но разными id
        assertEquals(task1, task2, "Задачи не совпадают.");

    }



    @Test
    void equalsEpicWithSameId() { // проверка на равенство двух экземпляров наследников Task с одинаковым id
        Epic epic1 = new Epic("Test addNewTask", "Test addNewTask description");
        epic1.setHandId(1);

        Epic epic2 = new Epic("Test addNewTask", "Test addNewTask description");
        epic2.setHandId(1);

        // Проверка на равенство двух задач с одинаковыми полями, но разными id
        assertEquals(epic1, epic2, "Задачи не совпадают.");
    }

    @Test
    void EpicCantBeHimSubepic() { // проверка, что Эпик нельзя добавить в себя в виде подзадачи
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("подзадача-5", "первая подзадача к эпику 3", epic.getId());
        subtask.setId(epic.getId());
        assertFalse(epic.setSubId(epic.getId()));

    }

    @Test
    void subtaskCantBeHimEpic() { // проверка, что субтаск нельзя нельзя сделать своим эпиком
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("подзадача-5", "первая подзадача к эпику 3", epic.getId());

        assertFalse(subtask.setSubId(epic.getId()));
    }

    @Test
    void epicDoNotSaveIdRemovedSub() { // проверка, что эпик не хранит id удаленных субтасков
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("подзадача-5", "первая подзадача к эпику 3", epic.getId());
        taskManager.addSubTask(subtask);
        ArrayList<Integer> subId = epic.getEpicSub();
        assertEquals(subId.get(0), subtask.getId(), "Задачи не совпадают.");
        taskManager.removeByIdSubtask(subtask.getId());
        assertTrue(subId.isEmpty());
    }

    @Test
    void taskManageraAddAndReturnTask() { // проверка, что InMemoryTaskManager добавляет и возвращает задачи
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        int id = taskManager.addTask(task);
        final Task savedTask = taskManager.showTask(id);

        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        int idEpic = taskManager.addEpic(epic);
        final Epic savedEpic = taskManager.showEpic(idEpic);

        Subtask subtask = new Subtask("подзадача-5", "первая подзадача к эпику 3", idEpic);
        int subId = taskManager.addSubTask(subtask);
        final Epic saveSubtask = taskManager.showSubTask(subId);

        assertEquals(task, savedTask, "Задачи не совпадают.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
        assertEquals(subtask, saveSubtask, "Задачи не совпадают.");

    }

    @Test
        // проверка, что здачи с заданным и сгенерированным id не противоречат
    void automaticAndHandleSetId() {
        Task task1 = new Task("Test addNewTask", "Test addNewTask description");
        int taskId1 = taskManager.addTask(task1);
        Task task2 = new Task("Test addNewTask", "Test addNewTask description");
        task2.setHandId(3);
        taskManager.addTask(task2);

        Task savedTask1 = taskManager.showTask(taskId1);
        Task savedTask2 = taskManager.showTask(3);

        assertEquals(task1, savedTask1, "Задачи не совпадают.");
        assertEquals(task2, savedTask2, "Задачи не совпадают.");

    }

}