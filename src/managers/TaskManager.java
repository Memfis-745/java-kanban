package managers;

import tasks.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface TaskManager {
    ArrayList<Task> getAllTask();


    ArrayList<Epic> getAllEpic();

    ArrayList<Subtask> getAllSubTask();

    public ArrayList<Task> getTreeSet();

    void removeAllTask();

    void removeAllEpic();

    void removeAllSubTask();

    Task showTask(int id);

    Epic showEpic(int id);

    Subtask showSubTask(int id);

    int addTask(Task task);

    Integer addEpic(Epic epic);

    int addSubTask(Subtask subtask);

    int updateTask(int id, Task task);

    int updateSubTask(int id, Subtask subtask);

    void removeByIdTask(int id);

    void removeByIdEpic(int id);

    void removeByIdSubtask(int id);

    ArrayList<Subtask> showSubtask(int id);

    List<Task> getHistory();

    void remove(int id);

    public void epicSubTime(Epic epic, Subtask subtask);

    public Set<Task> getPrioritizedTasks();

    public boolean validationTreeSet(Task task);

    public void nulId();
}
