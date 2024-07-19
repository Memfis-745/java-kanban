package managers;

import tasks.*;
import tasks.Status;


import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    public static Integer id = 0;
    final HashMap<Integer, Task> listOfTask = new HashMap<>();
    final HashMap<Integer, Epic> listOfEpic = new HashMap<>();
    final HashMap<Integer, Subtask> listOfSubTask = new HashMap<>();

    Comparator<Task> userComparator = Comparator.comparing(Task::getStart);
    Set<Task> taskTreeSet = new TreeSet<>(userComparator);
    HistoryManager historyManager = Managers.getDefaultHistory();

    @Override                      // 1.1 Возвращает список задач
    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(listOfTask.values());
    }


    @Override                       // 1.2 Возвращает список эпиков
    public ArrayList<Epic> getAllEpic() {

        return new ArrayList<>(listOfEpic.values());
    }

    @Override
    public ArrayList<Task> getTreeSet() { // 1. Вывести список Трисетов"
        return new ArrayList<>(taskTreeSet);
    }

    @Override                       // 1.3 Возвращает список подзазач
    public ArrayList<Subtask> getAllSubTask() {

        return new ArrayList<>(listOfSubTask.values());
    }

    @Override
    public void removeAllTask() { // 2.1. Удалить все задачи"
        listOfTask.clear();
        nulId();
    }

    @Override
    public void removeAllEpic() { // 2.2 Удалить все эпики
        listOfEpic.clear();
        listOfSubTask.clear();
        nulId();
    }

    @Override
    public void removeAllSubTask() { // 2.3 Удалить все подзадачи"
        listOfSubTask.clear();

        for (Epic epic : listOfEpic.values()) {
            epic.clearEpicSub();
            epic.reNewEpicStatus(listOfSubTask);
        }
        nulId();
    }

    @Override                           // 3.1 Возвращает задачу по id
    public Task showTask(int id) {
        historyManager.addHistory(listOfTask.get(id));
        return listOfTask.get(id);
    }

    @Override                            // 3.2 Возвращает эпик по id
    public Epic showEpic(int id) {
        historyManager.addHistory(listOfEpic.get(id));
        return listOfEpic.get(id);
    }

    @Override                             // 3.3 Возвращает подзадачу по id
    public Subtask showSubTask(int id) {
        historyManager.addHistory(listOfSubTask.get(id));
        return listOfSubTask.get(id);
    }

    @Override
    public int addTask(Task task) {  // 4.1    создание задачи
        int id;
        if ((task.getId() == null) || (task.getId() == 0)) {
            id = getId();
        } else if (reviewId(task.getId())) {
            System.out.println("Данный Id занят новый присвоен автоматически");
            id = getId();
        } else {
            id = task.getId();
        }

        task.setId(id);
        task.setStatus(Status.NEW);

        task.getFinish();
        listOfTask.put(id, task);
        validationTreeSet(task);
        return id;
    }

    @Override
    public Integer addEpic(Epic epic) {   // 4.2    создание эпика
        int id;
        if ((epic.getId() == null) || (epic.getId() == 0)) {
            id = getId();
        } else if (reviewId(epic.getId())) {
            System.out.println("Данный Id существует новый присвоен автоматически");
            id = getId();
        } else {
            id = epic.getId();
        }
        epic.setId(id);
        epic.setStatus(Status.NEW);

        listOfEpic.put(id, epic);
        return id;
    }

    @Override
    public int addSubTask(Subtask subtask) { // 4.3    создание подзадачи
        int id;
        if ((subtask.getId() == null) || (subtask.getId() == 0)) {
            id = getId();
        } else if (reviewId(subtask.getId())) {
            System.out.println("Данный Id существует, новый присвоен автоматически");
            id = getId();
        } else {
            id = subtask.getId();
        }

        subtask.setSubId(id);
        int epicId = subtask.getEpicId();
        subtask.setStatus(Status.NEW);

        listOfSubTask.put(id, subtask);
        Epic epic = listOfEpic.get(epicId);

        epic.setSubId(id);  // присваиваем значение элементу списка в Эпике с новой подзадачей

        epicSubTime(epic, subtask);
        validationTreeSet(subtask);
        return id;
    }

    @Override
    public void epicSubTime(Epic epic, Subtask subtask) {    // счетчик

        if (epic.getEpicStart() == null || epic.getEpicStart().isAfter(subtask.getStart())) {
            epic.setEpicStart(subtask.getStart());
        }

        if (epic.getEpicFinish() == null || epic.getEpicFinish().isBefore(subtask.getFinish())) {
            epic.setEpicFinish(subtask.getFinish());
        }
        epic.setDuration(epic.getEpicDuration().plus(subtask.getDuration()));
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return taskTreeSet;
    }

    @Override
    public boolean validationTreeSet(Task task) {
        if (task.getStart() == null) {
            System.out.println("Начальное время не задано");
            return false;
        }
        if (taskTreeSet.isEmpty()) {
            taskTreeSet.add(task);
            return true;
        } else {
            boolean result = taskTreeSet.stream()
                    .allMatch(taskTree -> {

                                if (taskTree.getStart().isBefore(task.getStart())
                                        && (taskTree.getFinish().isBefore(task.getStart()))) {
                                    return true;
                                } else if ((task.getStart().isBefore(taskTree.getStart())
                                        && (task.getFinish().isBefore(taskTree.getStart())))) {
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                    );
            if (result) {
                //   System.out.println("Задача добавленная в трисет: " + task);
                taskTreeSet.add(task);
                return true;
            } else {
                System.out.println("Установите другой временной интервал у задачи: " + task);

            }

        }
        return false;
    }

    @Override
    public int updateTask(int id, Task taskUpd) { // 5.1 изменение статуса задачи
        if (taskUpd == null) {
            System.out.println("Входящая задача пуста");
            return 404;
        }
        if (!listOfTask.containsKey(id)) {
            System.out.println("Такой задачи нет");
            return 404;
        }
        Task taskSaved = listOfTask.get(id);
        taskTreeSet.remove(taskSaved);
        if (validationTreeSet(taskUpd)) {
            listOfTask.put(id, taskUpd);
        } else {
            validationTreeSet(taskSaved);
            System.out.println("Обновленная задача пересекается по времени с существующими");
            return 406;

        }

        return 201;
    }

    @Override                                           // 5.2 изменение статуса подзадачи
    public int updateSubTask(int id, Subtask subtaskUpd) {

        if (subtaskUpd == null) {
            System.out.println("Входящая подзадача пуста");
            return 404;
        }
        if (!listOfSubTask.containsKey(id)) {
            System.out.println("Такой подзадачи нет");
            return 404;
        }
        Subtask subtaskSaved = listOfSubTask.get(id);
        taskTreeSet.remove(subtaskSaved);

        if (validationTreeSet(subtaskUpd)) {
            listOfSubTask.put(id, subtaskUpd);
        } else {
            validationTreeSet(subtaskSaved);
            System.out.println("Обновленная подзадача пересекается по времени с существующими");
            return 406;
        }

        int epicId = subtaskSaved.getEpicId();
        Epic epic = listOfEpic.get(epicId);
        epic.reNewEpicStatus(listOfSubTask);
        listOfEpic.put(epicId, epic);    // обновляем статус эпика.
        return 201;
    }


    @Override
    public void removeByIdTask(int id) {   // 6.1 Удалить задачу по идентификатору
        if (listOfTask.containsKey(id)) {
            listOfTask.remove(id);
        }
    }

    @Override
    public void removeByIdEpic(int id) {    // 6.2 Удалить эпик по идентификатору
        if (listOfEpic.containsKey(id)) {
            Epic epic = listOfEpic.get(id);
            for (int e : epic.getEpicSub()) {
                listOfSubTask.remove(e);
            }
            listOfEpic.remove(id);
        }
    }

    @Override
    public void removeByIdSubtask(int id) {     // 6.3 Удалить подзадачу по идентификатору
        if (listOfSubTask.containsKey(id)) {
            Subtask subtask = listOfSubTask.get(id);
            int epicId = subtask.getEpicId();
            Epic epic = listOfEpic.get(epicId);
            epic.removeSubId(id);
            listOfSubTask.remove(id);
            epic.setEpicStart(null);
            epic.setEpicFinish(null);
            epic.setDuration(null);
            if (!epic.getEpicSub().isEmpty()) {
                for (int i : epic.getEpicSub()) {
                    epicSubTime(epic, listOfSubTask.get(i));
                }
            }
        }
    }

    @Override
    public ArrayList<Subtask> showSubtask(int id) { // 7 показать подзадачи эпика

        ArrayList<Subtask> sub = null;
        if (listOfEpic.containsKey(id)) {
            Epic epic = listOfEpic.get(id);
            sub = new ArrayList();
            for (int s : epic.getEpicSub()) {
                sub.add(listOfSubTask.get(s));
            }
        } else {
            System.out.println("Такого эпика нет");
        }
        return sub;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void remove(int id) {
        historyManager.remove(id);
    }

    public int getId() {    // счетчик
        id++;
        int tempId = id;
        if (reviewId(tempId)) {
            while (listOfTask.containsKey(tempId) || listOfEpic.containsKey(tempId) || listOfSubTask.containsKey(tempId)) {
                tempId++;
                id = tempId;
            }
        }
        return id;
    }

    public void nulId() {    // счетчик
        if (listOfSubTask.isEmpty() && listOfEpic.isEmpty() && listOfTask.isEmpty()) {
            id = 0;
        }
    }

    public boolean reviewId(int idTemp) {

        return listOfTask.containsKey(idTemp) || listOfEpic.containsKey(idTemp) || listOfSubTask.containsKey(idTemp);
    }

}


