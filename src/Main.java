import java.util.ArrayList;


//import managers.InMemoryTaskManager;
import managers.*;
import tasks.*;

public class Main {

    static final InMemoryTaskManager taskManager = (InMemoryTaskManager) Managers.getDefault();
    public static void main(String[] args) {


        taskManager.addTask(new Task("Задача-1", "Описание задачи-1"));
        taskManager.addTask(new Task("Задача-2", "Описание задачи-2"));
        Integer epicId1 = taskManager.addEpic(new Epic("эпик-3", "описание эпика-3"));
        Integer epicId2 = taskManager.addEpic(new Epic("эпик-4", "описание эпика-4"));
        taskManager.addSubTask(new Subtask("подзадача-5", "первая подзадача к эпику 3", epicId1));
        taskManager.addSubTask(new Subtask("подзадача-6", "вторая подзадача к эпику 3", epicId1));
        taskManager.addSubTask(new Subtask("подзадача-7", "первая подзадача к эпику 4", epicId2));

        printTask();



        // открыть задачу, эпик , подзадачу по id
        // int inTask = 1;
        System.out.println("");
        System.out.println("Показать задачи, подзадачи и эпики :");
        Task task = taskManager.showTask(1);
        System.out.println(task);
        Epic epic = taskManager.showEpic(3);
        System.out.println(epic);
        Subtask subTask = taskManager.showSubTask(5);
        System.out.println(subTask);


        // смена статуса
        System.out.println("");
        System.out.println("Смена статуса задач, подзадач и эпиков :");
        // int idStatus = 1;

        taskManager.updateTask(1, "IN_PROGRESS");
        taskManager.updateSubTask(5, "DONE");
        taskManager.updateSubTask(6, "DONE");
        printTask();

        System.out.println("");
        System.out.println("Показать подзадачи эпика 4"); // показать подзадачи эпика

        ArrayList<Subtask> sub = taskManager.showSubtask(4);
        for (Subtask s : sub) {
            System.out.println(s);
        }
        System.out.println("");
        System.out.println("Показать историю вызовов");
       taskManager.showTask(1);
        taskManager.showTask(1);
       taskManager.showEpic(3);
       taskManager.showSubTask(5);
        taskManager.showSubTask(6);
        taskManager.showSubTask(7);
        taskManager.showTask(2);
        taskManager.showEpic(4);
        taskManager.showEpic(3);
        taskManager.showSubTask(5);
        taskManager.showSubTask(6);
        taskManager.showSubTask(7);
        taskManager.showTask(2);

        for(Task hist : taskManager.getHistory()) {
            System.out.println(hist);
        }

        System.out.println("Удаление из истории по id: ");
        taskManager.remove(5);


        for(Task hist : taskManager.getHistory()) {
            System.out.println(hist);
        }

        System.out.println("Удаление по id :");
        taskManager.removeByIdTask(2);
        taskManager.removeByIdEpic(3);
        taskManager.removeByIdSubtask(4);
        printTask();



        // удалить все
        System.out.println("Удалить все задачи:");
        taskManager.removeAllTask();
        taskManager.removeAllEpic();
        taskManager.removeAllSubTask();
        printTask();



    }




    public static void printTask() {
        System.out.println("Список задач: ");
        for (Task task : taskManager.getAllTask()) {
            System.out.println(task);
        }

        System.out.println("Список эпиков: ");
        for (Epic epic : taskManager.getAllEpic()) {
            System.out.println(epic);
        }

        System.out.println("Список подзадач: ");
        for (Epic epic : taskManager.getAllSubTask()) {
            System.out.println(epic);
        }
    }
}


