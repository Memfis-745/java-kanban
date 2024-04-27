import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    public static Integer id = 0;
    final HashMap<Integer, Task> listOfTask = new HashMap<>();
    final HashMap<Integer, Epic> listOfEpic = new HashMap<>();
    final HashMap<Integer, Subtask> listOfSubTask = new HashMap<>();


    public ArrayList<Task> getAllTask() { // 1. Вывести список всех задач"
        return new ArrayList<>(listOfTask.values());
    }

    public ArrayList<Epic> getAllEpic() { // 1. Вывести список всех задач"
        return new ArrayList<>(listOfEpic.values());
    }

    public ArrayList<Subtask> getAllSubTask() { // 1. Вывести список всех задач"
        return new ArrayList<>(listOfSubTask.values());
    }

    public void removeAllTask() { // 2. Удалить все задачи"
        listOfTask.clear();
        nulId();
    }

    public void removeAllEpic() { // 2. Удалить все задачи"
        listOfEpic.clear();
        listOfSubTask.clear();
        nulId();
    }

    public void removeAllSubTask() { // 2. Удалить все задачи"
        listOfSubTask.clear();
        nulId();
    }

    public Task showTask(int id) { // 3. Открыть задачу по идентификатору
        return listOfTask.get(id);
    }

    public Epic showEpic(int id) { // 3. Открыть задачу по идентификатору
        return listOfEpic.get(id);
    }

    public Subtask showSubTask(int id) { // 3. Открыть задачу по идентификатору
        return listOfSubTask.get(id);
    }

    public void addTask(Task task) {  // создание задачи
        int id = getId();
        task.setId(id);
        task.setStatus(Status.NEW);
        listOfTask.put(id, task);
    }

    public void addEpic(Epic epic) { //    создание эпика
        int id = getId();
        epic.setId(id);
        epic.setStatus(Status.NEW);
        listOfEpic.put(id, epic);
    }

    public void addSubTask(Subtask subtask) { // создание подзадачи
        int subId = getId();
        subtask.setId(id);
        subtask.setStatus(Status.NEW);
        listOfSubTask.put(subId, subtask);
        int epicId = subtask.getEpicId();
        Epic epic = listOfEpic.get(epicId);
        epic.setSubId(subId);  // присваиваем значение элементу списка в Эпике с новой подзадачей

    }

    public void updateTask(int id, String status) { // метод изменения статуса
        if (listOfTask.containsKey(id)) {
            Task task = listOfTask.get(id);
            switch (Status.valueOf(status)) { // меняем статус задачи
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


    public void removeByIdTask(int id) {   // 6. Удалить задачу по идентификатору

        if (listOfTask.containsKey(id)) {
            listOfTask.remove(id);
        }
    }

    public void removeByIdEpic(int id) {
        if (listOfEpic.containsKey(id)) {
            Epic epic = listOfEpic.get(id);
            for (int e : epic.getEpicSub()) {
                listOfSubTask.remove(e);
            }
            listOfEpic.remove(id);
        }
    }

    public void removeByIdSubtask(int id) {
        if (listOfSubTask.containsKey(id)) {
            Subtask subtask = listOfSubTask.get(id);
            int epicId = subtask.getEpicId();
            Epic epic = listOfEpic.get(epicId);
            epic.removeSubId(id);
            listOfSubTask.remove(id);

        }
    }

    public ArrayList<Subtask> showSubtask(int id) { // показать подзадачи эпика

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

    public int getId() {    // счетчик
        id++;
        return id;
    }

    public void nulId() {    // счетчик
        if (listOfSubTask.isEmpty() && listOfEpic.isEmpty() && listOfTask.isEmpty()) {
            id = 0;
        }
    }

}
