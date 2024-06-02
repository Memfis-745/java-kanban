package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    final HashMap<Integer, Node> hashHistory = new HashMap<>();
    int hashIdTail = 0;
    int hashId = 0;
    public List<Task> history = new ArrayList<>();
    Node prev;

    @Override
    public void addHistory(Task task) {
        if (task != null) {
            linkLast(task);
            getTasks();
        } else {
            System.out.println("Объект пустой");
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> getHistory = history;
        return getHistory;
    }

    @Override
    public void remove(int id) {
        removeNode(hashHistory.get(id));
        getTasks();
    }

    public void linkLast(Task task) {
        if (hashHistory.isEmpty()) {
            prev = new Node(null, task, null);
            hashIdTail = task.getId();
            hashHistory.put(hashIdTail, prev);// первый элемент
            hashId = hashIdTail;

        } else {
            int hashIdPrev = hashId;
            hashId = task.getId();
            if (hashHistory.containsKey(hashId)) {
                Node nodeReplace = hashHistory.get(hashId);

                if (nodeReplace.prev != null) {
                    if (nodeReplace.next != null) {
                        nodeReplace.prev.next = nodeReplace.next;
                        nodeReplace.next.prev = nodeReplace.prev;
                    }
                } else {
                    if (nodeReplace.next != null) {
                        nodeReplace.next.prev = null;
                        Node next = nodeReplace.next;
                        Task taskNext = (Task) next.data;
                        hashIdTail = taskNext.getId();
                    } else {
                        hashIdTail = task.getId();
                    }
                }
            }
            hashHistory.put(hashId, new Node(hashHistory.get(hashIdPrev), task, null));
            Node node = hashHistory.get(hashIdPrev);
            if (hashId != hashIdPrev) {
                node.next = hashHistory.get(hashId);
            }
        }
    }

    public void getTasks() {

        history.clear();
        Node nod = hashHistory.get(hashIdTail);
        history.add((Task) nod.data);


        Node nodeNext;
        if (nod.next == null) {
            return;
        } else {
            nodeNext = nod.next;
        }

        while (nodeNext != null) {
            Task task = (Task) nodeNext.data;
            history.add(task);
            nodeNext = nodeNext.next;
        }
    }

    public void removeNode(Node node) {
        Task task = (Task) node.data;
        if (node.prev != null) {
            if (node.next == null) {
                node.prev.next = null;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
        } else {
            if (node.next != null) {
                node.next.prev = null;

                Task task1 = (Task) node.next.data;
                hashIdTail = task1.getId();
            }
        }
        hashHistory.remove(task.getId());

    }
}



