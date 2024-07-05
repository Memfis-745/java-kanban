import managers.FileBackedTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.Status.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    abstract void initial();

    @Test
    void getAllTask() { // 1.1 показать все задачи
        Task task1 = new Task("Задача-1", "Описание задачи-1", Duration.ofMinutes(120), LocalDateTime.of(2024, 1, 1, 0, 0));
        taskManager.addTask(task1);
        Task task2 = new Task("Задача-2", "Описание задачи-2", Duration.ofMinutes(100), LocalDateTime.of(2024, 1, 1, 0, 0));
        taskManager.addTask(task2);

        List<Task> taskList = taskManager.getAllTask();
        assertEquals(2, taskList.size(), "В списке должно быть две задачи");
        assertEquals(task1, taskList.get(0), "Задача должна быть первой");
        assertEquals(task2, taskList.get(1), "Задача должна быть второй");
    }

    @Test
    void getAllEpic() { // 1.2 показать все эпики
        Epic epic1 = new Epic("эпик-3", "описание эпика-3");
        Epic epic2 = new Epic("эпик-4", "описание эпика-4");
        Integer epicId1 = taskManager.addEpic(epic1);
        Integer epicId2 = taskManager.addEpic(epic2);
        List<Epic> epicList = taskManager.getAllEpic();
        assertEquals(2, epicList.size(), "В списке должно быть два эпика");
        assertEquals(epic1, epicList.get(0), "Задача должна быть первой");
        assertEquals(epic2, epicList.get(1), "Задача должна быть второй");
    }

    @Test
    void getAllSubTasks() { // 1.3 показать все субтаски
        Integer epicId1 = taskManager.addEpic(new Epic("эпик-3", "описание эпика-3"));
        ;
        Subtask subTask1 = new Subtask("подзадача-5", "первая подзадача к эпику 3", epicId1, Duration.ofMinutes(103), LocalDateTime.of(2024, 1, 1, 0, 0));
        Subtask subTask2 = new Subtask("подзадача-6", "вторая подзадача к эпику 3", epicId1, Duration.ofMinutes(110), LocalDateTime.of(2024, 2, 1, 0, 0));
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        List<Subtask> subTaskList = taskManager.getAllSubTask();
        assertEquals(2, subTaskList.size(), "В списке должно быть два эпика");
        assertEquals(subTask1, subTaskList.get(0), "Задача должна быть первой");
        assertEquals(subTask2, subTaskList.get(1), "Задача должна быть второй");
    }

    @Test
    void removeAllTask() { // 2.1 Удалить все задачи
        Task task1 = new Task("Задача-1", "Описание задачи-1", Duration.ofMinutes(120), LocalDateTime.of(2024, 1, 1, 0, 0));
        taskManager.addTask(task1);
        Task task2 = new Task("Задача-2", "Описание задачи-2", Duration.ofMinutes(100), LocalDateTime.of(2024, 1, 1, 0, 0));
        taskManager.addTask(task2);
        taskManager.removeAllTask();
        assertEquals(0, taskManager.getAllTask().size(), "Список задач не пуст");
    }

    @Test
    void removeAllEpic() {   // 2.2 Удалить все эпики
        Epic epic1 = new Epic("эпик-3", "описание эпика-3");
        Epic epic2 = new Epic("эпик-4", "описание эпика-4");
        Integer epicId1 = taskManager.addEpic(epic1);
        Integer epicId2 = taskManager.addEpic(epic2);
        taskManager.removeAllEpic();
        assertEquals(0, taskManager.getAllTask().size(), "Список эпиков не пуст");
    }

    @Test
    void removeAllSubTask() {              // 2.3 Удалить все подзадачи
        taskManager.removeAllSubTask();
        Integer epicId1 = taskManager.addEpic(new Epic("эпик-3", "описание эпика-3"));
        ;
        Subtask subTask1 = new Subtask("подзадача-5", "первая подзадача к эпику 3", epicId1, Duration.ofMinutes(103), LocalDateTime.of(2024, 1, 1, 0, 0));
        Subtask subTask2 = new Subtask("подзадача-6", "вторая подзадача к эпику 3", epicId1, Duration.ofMinutes(110), LocalDateTime.of(2024, 2, 1, 0, 0));
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        assertEquals(2, taskManager.getAllSubTask().size(), "Список подзадач не равен 2");
        taskManager.removeAllSubTask();
        assertEquals(0, taskManager.getAllSubTask().size(), "Список подзадач не пуст");
    }

    @Test
    void showTask() {  // 3.1 Показать Задачу по id
        taskManager.removeAllTask();
        Task task = new Task("Задача-1", "Описание задачи-1", Duration.ofMinutes(120), LocalDateTime.of(2024, 1, 1, 0, 0));
        taskManager.addTask(task);
        assertEquals(task, taskManager.showTask(1), "Задачи не совпадают");
    }

    @Test
    void showEpic() { // 3.2 Показать эпик по id
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        int idEpic = taskManager.addEpic(epic);
        assertEquals(epic, taskManager.showEpic(idEpic), "Задачи не совпадают");
    }

    @Test
    void showSubTask() {  // 3.3 Показать подзадачу по id
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        int idEpic = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("подзадача-5", "первая подзадача к эпику 3", idEpic, Duration.ofMinutes(103), LocalDateTime.of(2024, 1, 1, 0, 0));
        int subId = taskManager.addSubTask(subtask);
        assertEquals(subtask, taskManager.showSubTask(subId), "Задачи не совпадают");
    }

    @Test
    void addTask() {                      // 4.1 Создать задачу
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        Task task = new Task("Задача-1", "Описание задачи-1", Duration.ofMinutes(120), startTime);
        taskManager.addTask(task);
        assertNotNull(task, "Задача не создана");
        assertTrue(task.getId() > 0, "id не присвоен");
        assertEquals(startTime, task.getStart(), "Не верное время начала задачи");
        assertEquals(Duration.ofMinutes(120), task.getDuration(), "Не верная длительность задачи");
        assertEquals(startTime.plus(Duration.ofMinutes(120)), task.getFinish(), " Не верное время окончания задачи");
        assertEquals(task, taskManager.showTask(task.getId()), "Задача не записана в Мап");
    }

    @Test
    void addEpic() {                     //4.2 Создать Эпик
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        int idEpic = taskManager.addEpic(epic);
        assertNotNull(epic, "Эпик не создан");
        assertTrue(epic.getId() > 0, "Не правильно высчитан id");
        assertEquals(epic, taskManager.showEpic(idEpic), "Эпик не записан в Мап");
    }

    @Test
    void addSubTask() {                     // 4.3 Создать подзадачу
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        int epicId = taskManager.addEpic(epic);
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        Subtask subtask = new Subtask("подзадача-5", "первая подзадача к эпику 3", epicId, Duration.ofMinutes(103), startTime);
        taskManager.addSubTask(subtask);

        assertNotNull(subtask, "Ошибка при создании субтаска");
        assertTrue(subtask.getId() > 0, "Не правильно высчитан id");
        assertEquals(epicId, subtask.getEpicId(), "Не верный id эпика");
        assertEquals(startTime, subtask.getStart(), "Не верное время начала задачи");
        assertEquals(Duration.ofMinutes(103), subtask.getDuration(), "Не верная длительность задачи");
        assertEquals(startTime.plus(Duration.ofMinutes(103)), subtask.getFinish(), "Не правильное время окончания задачи");
        assertEquals(epic, taskManager.showEpic(subtask.getEpicId()), "Эпик не записан в Мап");
    }

    void updateTaskStatus() { // 5.1 изменения статуса задачи
        Task task = new Task("Задача-1", "Описание задачи-1", Duration.ofMinutes(120), LocalDateTime.of(2024, 1, 1, 0, 0));
        taskManager.addTask(task);
        taskManager.updateTask(1, "IN_PROGRESS");
        assertEquals(IN_PROGRESS, task.getStatus(), "Статус не изменился");
    }

    @Test
        //5.2 изменения статуса задачи
    void StatusEpicSeparateTest() throws IOException { // эпик и подзадачи со статусами NEW
        final File file = new File("taskFileTest.csv");
        final FileBackedTaskManager fileManagerOut = new FileBackedTaskManager(file);

        Epic epic = new Epic("эпик-3", "описание эпика-3");
        Integer epicId = fileManagerOut.addEpic(epic);
        fileManagerOut.save();

        Subtask subtask = new Subtask("подзадача-5", "первая подзадача к эпику 3", epic.getId(), Duration.ofMinutes(103), LocalDateTime.of(2024, 1, 1, 0, 0));
        Subtask subtask1 = new Subtask("подзадача-6", "вторая подзадача к эпику 3", epic.getId(), Duration.ofMinutes(110), LocalDateTime.of(2024, 2, 1, 0, 0));
        fileManagerOut.addSubTask(subtask);
        fileManagerOut.save();
        fileManagerOut.addSubTask(subtask1);
        fileManagerOut.save();

        int subtaskId = subtask.getId();
        int subtaskId1 = subtask1.getId();

        final FileBackedTaskManager fileManagerIn = FileBackedTaskManager.loadFromFile(file);

        // 1. Эпик и подзадачи имеют статус NEW и равны друг другу
        assertEquals(fileManagerIn.showEpic(epicId).getStatus(), fileManagerIn.showSubTask(subtaskId).getStatus());
        assertEquals(fileManagerIn.showEpic(epicId).getStatus(), fileManagerIn.showSubTask(subtaskId1).getStatus());

        //2. Меняем статус одной из подзадач на DONE. Эпик - NEW, подзадача DONE
        fileManagerIn.updateSubTask(subtaskId, "DONE");
        assertEquals(fileManagerIn.showEpic(epicId).getStatus(), NEW);
        assertEquals(fileManagerIn.showSubTask(subtaskId).getStatus(), DONE);

        //3. Меняем статус второй подзадачи эпика на DONE. Эпик стал DONE
        fileManagerIn.updateSubTask(subtaskId1, "DONE");
        assertEquals(fileManagerIn.showEpic(epicId).getStatus(), DONE);

        //4. Меняем статус обоих эпиков на IN_PROGRESS. Эпик тоже становится IN_PROGRESS
        fileManagerIn.updateSubTask(subtaskId, "IN_PROGRESS");
        fileManagerIn.updateSubTask(subtaskId1, "IN_PROGRESS");
        assertEquals(fileManagerIn.showEpic(epicId).getStatus(), IN_PROGRESS);
    }

    @Test
    void removeByIdTask() {  // 6.1 Удаление задачи по id
        taskManager.removeAllTask();
        Task task = new Task("Задача-1", "Описание задачи-1", Duration.ofMinutes(120), LocalDateTime.of(2024, 1, 1, 0, 0));
        taskManager.addTask(task);
        taskManager.removeByIdTask(1);
        assertEquals(0, taskManager.getAllTask().size(), "Список задач не пуст");
    }

    @Test
    void removeByIdEpic() {
        taskManager.removeAllEpic();
        Epic epic = new Epic("эпик-3", "описание эпика-3");
        int id = taskManager.addEpic(epic);
        taskManager.removeByIdEpic(id);
        assertEquals(0, taskManager.getAllEpic().size(), "Список эпиков не пуст");
    }

    @Test
    void removeByIdSubtask() {
        taskManager.removeAllSubTask();
        taskManager.removeAllEpic();
        Epic epic = new Epic("эпик-3", "описание эпика-3");
        Integer epicId = taskManager.addEpic(epic);

        Subtask subtask = new Subtask("подзадача-5", "первая подзадача к эпику 3", epicId, Duration.ofMinutes(103), LocalDateTime.of(2024, 1, 1, 0, 0));
        Subtask subtask1 = new Subtask("подзадача-6", "вторая подзадача к эпику 3", epicId, Duration.ofMinutes(110), LocalDateTime.of(2024, 2, 1, 0, 0));
        taskManager.addSubTask(subtask);
        taskManager.addSubTask(subtask1);

        int subtaskId = subtask.getId();
        int subtaskId1 = subtask1.getId();

        taskManager.removeByIdSubtask(subtaskId1);
        assertEquals(1, taskManager.getAllSubTask().size(), "Количество подзадач не равно 1");
        taskManager.removeByIdEpic(epicId);
        System.out.println(taskManager.getAllSubTask().size());
        assertEquals(0, taskManager.getAllSubTask().size(), "Количество подзадач не равно 0");
    }

    @Test
    void getSubTaskList() {
        Epic epic = new Epic("эпик-3", "описание эпика-3");
        Integer epicId = taskManager.addEpic(epic);

        Subtask subtask = new Subtask("подзадача-5", "первая подзадача к эпику 3", epicId, Duration.ofMinutes(103), LocalDateTime.of(2024, 1, 1, 0, 0));
        Subtask subtask1 = new Subtask("подзадача-6", "вторая подзадача к эпику 3", epicId, Duration.ofMinutes(110), LocalDateTime.of(2024, 2, 1, 0, 0));
        taskManager.addSubTask(subtask);
        taskManager.addSubTask(subtask1);

        int subtaskId = subtask.getId();
        int subtaskId1 = subtask1.getId();
        List<Subtask> list = taskManager.showSubtask(epicId);
        assertEquals(2, list.size(), "Список содержит два элемента");
    }

    @Test
    void getHistory() {
        taskManager.removeAllSubTask();
        taskManager.removeAllEpic();
        taskManager.removeAllTask();
        Task task = new Task("Задача-1", "Описание задачи-1", Duration.ofMinutes(120), LocalDateTime.of(2024, 1, 1, 0, 0));
        taskManager.addTask(task);
        Epic epic = new Epic("эпик-3", "описание эпика-3");
        Epic epic2 = new Epic("эпик-4", "описание эпика-3");

        Integer epicId = taskManager.addEpic(epic);
        Integer epicId2 = taskManager.addEpic(epic2);
        Subtask subtask = new Subtask("подзадача-5", "первая подзадача к эпику 3", epicId, Duration.ofMinutes(103), LocalDateTime.of(2024, 2, 1, 0, 0));
        taskManager.addSubTask(subtask);
        int idSub = subtask.getId();
        taskManager.showTask(1);
        taskManager.showEpic(epicId);
        taskManager.showSubTask(idSub);


        List<Task> list = taskManager.getHistory();
        int size = list.size();
        assertEquals(3, list.size(), "Список истории не содержит 3 элемента");
        assertEquals(task, list.get(0), "Задача не является первой");
        assertEquals(epic, list.get(1), "Эпик не является вторым");
        assertEquals(subtask, list.get(2), "Подзадача не является третьей");
        taskManager.showEpic(2);
        taskManager.showTask(1);

        List<Task> list1 = taskManager.getHistory();

        taskManager.showEpic(epicId2);
        System.out.println(list1 + " размер " + list1.size());
        assertEquals(epic2, list1.get(3), "Эпик не находится на четвертом месте");
        assertEquals(task, list1.get(2), "Задача не находится на пятом месте");
        taskManager.removeByIdTask(1);

        List<Task> list2 = taskManager.getHistory();
        assertEquals(4, list2.size(), "Размер истории не равен 5");
    }

    @Test
    void getPrioritizedTasks() {
        taskManager.removeAllSubTask();
        taskManager.removeAllEpic();
        taskManager.removeAllTask();

        Epic epic = new Epic("эпик-3", "описание эпика-3");
        Integer epicId = taskManager.addEpic(epic);

        Subtask subtask = new Subtask("подзадача-5", "первая подзадача к эпику 3", epicId, Duration.ofMinutes(103), LocalDateTime.of(2024, 1, 1, 0, 0));
        taskManager.addSubTask(subtask);
        Task task = new Task("Задача-1", "Описание задачи-1", Duration.ofMinutes(120), LocalDateTime.of(2024, 2, 1, 0, 0));
        taskManager.addTask(task);


        Set<Task> list1 = taskManager.getPrioritizedTasks();
        Iterator<Task> iterator1 = list1.iterator();
        assertEquals(subtask, iterator1.next(), "Подзадача не занимает первое место по приоритету");
        assertEquals(task, iterator1.next(), "Задача не занимает второе место по приоритету");
        // System.out.println(iterator1.next());


        Subtask subtask2 = new Subtask("подзадача-5", "первая подзадача к эпику 3", epicId, Duration.ofMinutes(103), LocalDateTime.of(2024, 3, 1, 0, 0));
        taskManager.addSubTask(subtask2);
        Set<Task> list2 = taskManager.getPrioritizedTasks();
        Iterator<Task> iterator2 = list2.iterator();
        assertEquals(subtask, iterator2.next(), "Подзадача не занимает первое место по приоритету");
        assertEquals(task, iterator2.next(), "Задача не занимает второе место по приоритету");
        assertEquals(subtask2, iterator2.next(), "Подзадача не занимает третье место по приоритету");
    }

    @Test
    void taskWithSameIdEquals() throws IOException { // проверка на равенство двух экземпляров задач с одинаковым id
        Task task1 = new Task("Задача-1", "Описание задачи-1", Duration.ofMinutes(120), LocalDateTime.of(2024, 1, 1, 0, 0));
        task1.setHandId(1);
        taskManager.addTask(task1);

        final Task savedTask = taskManager.showTask(1);
        final Task savedTaskCopy = taskManager.showTask(1);

        // Проверка на равенство двух задач с одинаковыми полями, но разными id
        assertEquals(task1, savedTaskCopy, "Задачи не совпадают.");
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
    void EpicCantBeHimSubepic() throws IOException { // проверка, что Эпик нельзя добавить в себя в виде подзадачи
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("подзадача-5", "первая подзадача к эпику 3", epic.getId(), Duration.ofMinutes(103), LocalDateTime.of(2024, 1, 1, 0, 0));
        subtask.setId(epic.getId());
        assertFalse(epic.setSubId(epic.getId()));

    }

    @Test
    void subtaskCantBeHimEpic() throws IOException { // проверка, что субтаск нельзя нельзя сделать своим эпиком
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("подзадача-5", "первая подзадача к эпику 3", epic.getId(), Duration.ofMinutes(103), LocalDateTime.of(2024, 1, 1, 0, 0));

        assertFalse(subtask.setSubId(epic.getId()));
    }

    @Test
    void epicDoNotSaveIdRemovedSub() throws IOException { // проверка, что эпик не хранит id удаленных субтасков
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("подзадача-5", "первая подзадача к эпику 3", epic.getId(), Duration.ofMinutes(103), LocalDateTime.of(2024, 1, 1, 0, 0));
        taskManager.addSubTask(subtask);
        ArrayList<Integer> subId = epic.getEpicSub();
        assertEquals(subId.get(0), subtask.getId(), "Задачи не совпадают.");
        taskManager.removeByIdSubtask(subtask.getId());
        assertTrue(subId.isEmpty());
    }

    @Test
    void taskManageraAddAndReturnTask() throws IOException { // проверка, что InMemoryTaskManager добавляет и возвращает задачи
        Task task = new Task("Задача-1", "Описание задачи-1", Duration.ofMinutes(120), LocalDateTime.of(2024, 1, 1, 0, 0));
        int id = taskManager.addTask(task);
        final Task savedTask = taskManager.showTask(id);

        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        int idEpic = taskManager.addEpic(epic);
        final Epic savedEpic = taskManager.showEpic(idEpic);

        Subtask subtask = new Subtask("подзадача-5", "первая подзадача к эпику 3", idEpic, Duration.ofMinutes(103), LocalDateTime.of(2024, 1, 1, 0, 0));
        int subId = taskManager.addSubTask(subtask);
        final Subtask saveSubtask = taskManager.showSubTask(subId);

        assertEquals(task, savedTask, "Задачи не совпадают.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
        assertEquals(subtask, saveSubtask, "Задачи не совпадают.");

    }

    @Test
        // проверка, что здачи с заданным и сгенерированным id не противоречат
    void automaticAndHandleSetId() throws IOException {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Duration.ofMinutes(120), LocalDateTime.of(2024, 1, 1, 0, 0));
        int taskId1 = taskManager.addTask(task1);
        Task task2 = new Task("Задача-2", "Описание задачи-2", Duration.ofMinutes(120), LocalDateTime.of(2024, 2, 1, 0, 0));
        task2.setHandId(3);
        taskManager.addTask(task2);

        Task savedTask1 = taskManager.showTask(taskId1);
        Task savedTask2 = taskManager.showTask(3);

        assertEquals(task1, savedTask1, "Задачи не совпадают.");
        assertEquals(task2, savedTask2, "Задачи не совпадают.");

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

    // assertFalse(epic.setSubId(epic.getId()));

}
