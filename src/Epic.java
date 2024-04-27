import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task{
    public ArrayList<Integer> epicSub = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public void setId (int setEpicId){
        this.id = setEpicId;
    }
    public void setSubId (int setSubId){
        epicSub.add(setSubId);
    }
    public void removeSubId(Integer subId){
        epicSub.remove(subId);
    }

    public void clearEpicSub(){
        epicSub.clear();
    }

    public ArrayList<Integer> getEpicSub(){
        return epicSub;
    }


    public void reNewEpicStatus (HashMap<Integer, Subtask> listOfSubTask){
        int n = 0;
        int p = 0;
        int d = 0;

        for (Integer epic : epicSub) {
            if (epic != null) {
                Subtask subtask = listOfSubTask.get(epic);

                switch (subtask.getStatus()) {
                    case NEW:
                        n++;
                        break;
                    case IN_PROGRESS:
                        p++;
                        break;
                    case DONE:
                        d++;
                        break;
                    default:
                        break;
                }
                if (epicSub.size() == n) {
                    taskStatus = Status.NEW;
                } else if (epicSub.size() == p) {
                    taskStatus = Status.IN_PROGRESS;
                } else if (epicSub.size() == d) {
                    taskStatus = Status.DONE;
                }
            } else {
                taskStatus = Status.NEW;
            }
        }
        // return taskStatus;

    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", taskStatus=" + taskStatus + " номера подзадач=" +epicSub+
                '}';
    }
}
