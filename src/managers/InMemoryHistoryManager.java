package managers;
import tasks.*;

import java.util.ArrayList;
import java.util.List;
public class InMemoryHistoryManager implements HistoryManager {
    int historyId = 0;
    public List<Task> history = new ArrayList<>();
    @Override
    public void addHistory(Task task){
        if (task!=null) {
            if (history.size() >= 10) {
                history.remove(0);
            }
            history.add(task);
        } else {
           System.out.println("Объект пустой");
        }
    }
    @Override
    public List <Task> getHistory(){
        return history;
    }
}

