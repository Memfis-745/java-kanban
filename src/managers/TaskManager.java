package managers;
import tasks.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getAllTask() throws IOException;

   /* static Task fromStringValue(String value) {
        return null;
    }*/

    ArrayList<Epic> getAllEpic() throws IOException;

    ArrayList<Subtask> getAllSubTask() throws IOException;

    void removeAllTask() throws IOException;

    void removeAllEpic() throws IOException;

    void removeAllSubTask() throws IOException;

    Task showTask(int id) throws IOException;

    Epic showEpic(int id) throws IOException;

    Subtask showSubTask(int id) throws IOException;

    int addTask(Task task) throws IOException;

    Integer addEpic(Epic epic) throws IOException;

    int addSubTask(Subtask subtask) throws IOException;

    void updateTask(int id, String status) throws IOException;

    void updateSubTask(int id, String status) throws IOException;

    void removeByIdTask(int id) throws IOException;

    void removeByIdEpic(int id) throws IOException;

    void removeByIdSubtask(int id) throws IOException;

    ArrayList<Subtask> showSubtask(int id) throws IOException;

    public List<Task> getHistory() throws IOException;

   public void remove(int id) throws IOException;



}
