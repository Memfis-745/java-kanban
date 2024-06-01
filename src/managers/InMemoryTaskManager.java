package managers;
import tasks.*;
import tasks.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    public static Integer id = 0;
    public static Integer historyId = 0;
    final HashMap<Integer, Task> listOfTask = new HashMap<>();
    final HashMap<Integer, Epic> listOfEpic = new HashMap<>();
    final HashMap<Integer, Subtask> listOfSubTask = new HashMap<>();
    // public List<Task> history = new ArrayList<>();
    // public InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    private final HistoryManager historyManager;
    public InMemoryTaskManager(HistoryManager historyManager) {

        this.historyManager = historyManager;
    }

    @Override                      // 1.1 Возвращает список задач
    public ArrayList<Task> getAllTask() { // 1. Вывести список всех задач"
        return new ArrayList<>(listOfTask.values());
    }

    @Override                       // 1.2 Возвращает список эпиков
    public ArrayList<Epic> getAllEpic() { // 1. Вывести список всех задач"
        return new ArrayList<>(listOfEpic.values());
    }

    @Override                       // 1.1 Возвращает список подзазач
    public ArrayList<Subtask> getAllSubTask() { // 1. Вывести список всех задач"
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
    public Subtask showSubTask(int id) { //
        historyManager.addHistory(listOfSubTask.get(id));
        return listOfSubTask.get(id);
    }

    @Override
    public int addTask(Task task) {  // 4.1    создание задачи
        int id = getId();
        task.setId(id);
        task.setStatus(Status.NEW);

        int taskId = task.getId();
        if(reviewId(taskId)) {
            System.out.println("Ошибка ручной установки Id");
            taskId = id;
        }

        listOfTask.put(taskId, task);
        return taskId;

    }

    @Override
    public Integer addEpic(Epic epic) {   // 4.2    создание эпика
        int id = getId();
        epic.setId(id);
        epic.setStatus(Status.NEW);

        int epicId = epic.getId();
        if(reviewId(epicId)) {
            System.out.println("Ошибка ручной установки Id");
            epicId = id;
        }
        listOfEpic.put(epicId, epic);
        return epicId;
    }

    @Override
    public int addSubTask(Subtask subtask) { // 4.3    создание подзадачи
        int subId = getId();
        subtask.setSubId(id);
        int epicId = subtask.getEpicId();
        subtask.setStatus(Status.NEW);

        int subtId = subtask.getId();
        if(reviewId(subtId)) {
            System.out.println("Ошибка ручной установки Id");
            subtId = subId;
        }
        listOfSubTask.put(subtId, subtask);

        Epic epic = listOfEpic.get(epicId);
        epic.setSubId(subId);  // присваиваем значение элементу списка в Эпике с новой подзадачей
        return subtId;
    }

    @Override
    public void updateTask(int id, String status) { // 5.1 изменения статуса задачи
        if (listOfTask.containsKey(id)) {
            Task task = listOfTask.get(id);
            switch (Status.valueOf(status)) {
                case IN_PROGRESS:
                    task.setStatus(Status.IN_PROGRESS);
                    listOfTask.put(id, task);
                    break;
                case DONE:
                    task.setStatus(Status.DONE);
                    listOfTask.put(id, task);
                    break;
                default:
                    System.out.println("Ошибка");
            }
        } else {
            System.out.println("Такой задачи нет");
        }
    }

    @Override                                           // 5.2 изменение статуса подзадачи
    public void updateSubTask(int id, String status) {

        if (listOfSubTask.containsKey(id)) {

            Subtask subtask = listOfSubTask.get(id);
            int epicId = subtask.getEpicId();

            switch (Status.valueOf(status)) {   // меняем статус подзадачи
                case IN_PROGRESS:
                    subtask.setStatus(Status.IN_PROGRESS);
                    listOfSubTask.put(id, subtask);
                    break;
                case DONE:
                    subtask.setStatus(Status.DONE);
                    listOfSubTask.put(id, subtask);
                    break;
                default:
                    System.out.println("Ошибка");
            }
            Epic epic = listOfEpic.get(epicId);
            epic.reNewEpicStatus(listOfSubTask);
            listOfEpic.put(epicId, epic);    // обновляем статус эпика.

        } else if (listOfEpic.containsKey(id)) {
            System.out.println("Статус Эпика обновляется автоматически. Измените статусы подзадач");
        } else {
            System.out.println("Такой задачи нет");
        }
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
    public List<Task> getHistory(){
       return historyManager.getHistory();

    }
    @Override
    public void remove(int id){
         historyManager.remove(id);

    }

    public int getId() {    // счетчик
        id++;
        int tempId = id;
        if (reviewId(tempId)){
            while(listOfTask.containsKey(tempId) || listOfEpic.containsKey(tempId)||listOfSubTask.containsKey(tempId)){
                tempId++;
                id =  tempId;
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


