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
    Node prev = new Node(null, null, null);

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
        return history;
    }

    @Override
    public void remove(int id) {
       // getTasks();
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


                 if (nodeReplace.prev != null) {                       //если есть голова то
                     if (nodeReplace.next != null) {                    // проверяем наличие хвоста
                         nodeReplace.prev.next = nodeReplace.next;            // есть и хвост и голова - перепривязываем ссылки
                         nodeReplace.next.prev = nodeReplace.prev;                  // есть голова и нет хвоста - предыдущая нода некстом будет ссылаться на нул
                     }
                } else {                                     // если нет головы проверяем наличие хвоста
                        if (nodeReplace.next != null) {                 // проверяем есть ли хвост
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
            if ( hashId!=hashIdPrev){
                node.next = hashHistory.get(hashId);
            }
        }
    }

    public void getTasks() {

        history.clear();
        Node nod = hashHistory.get(hashIdTail);
        history.add((Task) nod.data); // добавляем первый элемент списка


        Node nodeNext;
        if (nod.next == null) {
            return;
        } else {
            nodeNext = nod.next; // присваиваем некст-ноду из первого элемента
        }

        while (nodeNext != null) {
            Task task = (Task) nodeNext.data;
            history.add(task);
            nodeNext = nodeNext.next;
        }
    }

    public void removeNode (Node node) {
        Task task = (Task) node.data;
        if (node.prev != null) {                       //если есть голова то
            if (node.next == null) {                    // проверяем наличие хвоста
                node.prev.next = null;                  // есть голова и нет хвоста - предыдущая нода некстом будет ссылаться на нул
            } else {
                node.prev.next = node.next;            // есть и хвост и голова - перепривязываем ссылки
                node.next.prev = node.prev;
            }
        } else {                                     // если нет головы проверяем наличие хвоста
            if (node.next != null) {                 // проверяем есть ли хвост
                node.next.prev = null;
                                                        // есть хвост и нет головы - следующая нода будет первой
               Task task1 = (Task) node.next.data;
                hashIdTail = task1.getId();
            }
        }                                             // нет головы и нет хвоста - просто удаляем.
         hashHistory.remove(task.getId());
         // hashHistory.values().remove(node);
    }
}




/*
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
 */

 /* System.out.println("текщая задача " + nod.data);
            System.out.println("текущее id " + hashId);
            System.out.println("текущий некст " + nod.next);
            System.out.println("текщая нода " + nod);
            System.out.println("");

            nod = hashHistory.get(hashIdPrev);
            System.out.println("предыдущая задача " + nod.data);
            System.out.println("предыдущее id "  + hashIdPrev);
            System.out.println("предыдущий некст " + nod.next);
            System.out.println("предыдущая нода " + nod);
            System.out.println("");*/

 /*  System.out.println("Нода будущего " + nodeNext);
            System.out.println("текущая задача " + task);
            System.out.println("текущая задача " + task.getId());
            System.out.println("история " + history);
            System.out.println("");*/

 /* System.out.println("перый nod " + nod);
        System.out.println("перый элемент " + nod.data);
        System.out.println("второй нод через первый некст " + nod.next);*/

 /*Node prevReplace = nodeReplace.prev;
                Node nextReplace = nodeReplace.next;
                if (prevReplace!=null) {
                    prevReplace.next = nextReplace;
                    nextReplace.prev = prevReplace;
                } else {
                    nextReplace.prev = null;
                }*/


      /* Node next1 = nodeReplace.next.prev;
                 //Node next = nodeReplace.next;
                // Task taskNextPrev = (Task) next1.data;
                // Task tasknext = (Task) next.data;
                 Node next1n = nodeReplace.next.next;
                 Node nextnextprev = nodeReplace.next.next.prev;
                 Task taskNextNext = (Task) next1n.data;
                 Task tasknextPrev = (Task) nextnextprev.data;
                 System.out.println("prev ID " + taskNextPrev.getId());
                // System.out.println("некст ID " + tasknext.getId());
                 System.out.println("prev ID " + taskNextNext.getId());
                 System.out.println("некст ID " + tasknextPrev.getId());
                    */