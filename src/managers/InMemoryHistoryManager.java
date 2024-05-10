package managers;
import tasks.*;

import java.util.ArrayList;
import java.util.List;
public class InMemoryHistoryManager implements HistoryManager {
    int historyId = 0;
    public List<Task> history = new ArrayList<>();
    @Override
    public void addHistory(Task task){
        if (historyId > 9) {

            history.remove(0);
            historyId--;
        }
        history.add(task);
        historyId++;
    }
    @Override
    public List <Task> getHistory(){
        return history;
    }
}

