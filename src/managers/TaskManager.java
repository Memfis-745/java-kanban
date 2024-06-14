package managers;
import tasks.*;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getAllTask();

    ArrayList<Epic> getAllEpic();

    ArrayList<Subtask> getAllSubTask();

    void removeAllTask();

    void removeAllEpic();

    void removeAllSubTask();

    Task showTask(int id);

    Epic showEpic(int id);

    Subtask showSubTask(int id);

    int addTask(Task task);

    Integer addEpic(Epic epic);

    int addSubTask(Subtask subtask);

    void updateTask(int id, String status);

    void updateSubTask(int id, String status);

    void removeByIdTask(int id);

    void removeByIdEpic(int id);

    void removeByIdSubtask(int id);

    ArrayList<Subtask> showSubtask(int id);

    public List<Task> getHistory();

   public void remove(int id);
}
