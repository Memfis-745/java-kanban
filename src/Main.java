import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;


//import managers.InMemoryTaskManager;
import managers.*;
import tasks.*;

public class Main {

    public static void main(String[] args) {
        final File file = new File("taskFile.csv");
        final FileBackedTaskManager fileManagerOut = new FileBackedTaskManager(file);


        fileManagerOut.addTask(new Task("Задача-1", "Описание задачи-1", Duration.ofMinutes(120), LocalDateTime.of(2024, 1, 1, 0, 0)));
        fileManagerOut.addTask(new Task("Задача-2", "Описание задачи-2", Duration.ofMinutes(100), LocalDateTime.of(2024, 1, 1, 0, 0)));
        Integer epicId1 = fileManagerOut.addEpic(new Epic("эпик-3", "описание эпика-3"));
        Integer epicId2 = fileManagerOut.addEpic(new Epic("эпик-4", "описание эпика-4"));
        fileManagerOut.addSubTask(new Subtask("подзадача-5", "первая подзадача к эпику 3", epicId1, Duration.ofMinutes(103), LocalDateTime.of(2024, 1, 1, 0, 0)));
        fileManagerOut.addSubTask(new Subtask("подзадача-6", "вторая подзадача к эпику 3", epicId1, Duration.ofMinutes(110), LocalDateTime.of(2024, 2, 1, 0, 0)));
        fileManagerOut.addSubTask(new Subtask("подзадача-7", "первая подзадача к эпику 4", epicId2, Duration.ofMinutes(90), LocalDateTime.of(2024, 3, 1, 0, 0)));

        System.out.println("\nСписок трисетов Созданных: ");
        for (Task task : fileManagerOut.getTreeSet()) {
            System.out.println(task);
        }
        printTask(fileManagerOut);

        FileBackedTaskManager fileManagerIn = FileBackedTaskManager.loadFromFile(file);
        System.out.println("\n\nПередача управления : ");

        System.out.println("Список трисетов Восстановленных: ");
        for (Task task : fileManagerIn.getTreeSet()) {
            System.out.println(task);
        }

        System.out.println("\n Список задач восстановленных: ");
        for (Task task : fileManagerIn.getAllTask()) {
            System.out.println(task);
        }

        System.out.println("Список эпиков восстановленных: ");
        for (Epic epic : fileManagerIn.getAllEpic()) {
            System.out.println(epic);
        }

        System.out.println("Список подзадач восстановленных: ");
        for (Subtask subtask : fileManagerIn.getAllSubTask()) {
            System.out.println(subtask);
        }

        System.out.println("\n");
        System.out.println("Показать задачи, подзадачи и эпики по id: 1, 3 и 5");
        Task task = fileManagerIn.showTask(1);
        System.out.println(task);
        Epic epic = fileManagerIn.showEpic(3);
        System.out.println(epic);
        Subtask subTask = fileManagerIn.showSubTask(5);
        System.out.println(subTask);


        // смена статуса
        System.out.println("\n");
        System.out.println("Смена статуса задач, подзадач и эпиков :");
        System.out.println("Смена статуса здачи-1 на: IN_PROGRESS");
        System.out.println("Одна из двух подзадач с id 5 к эпику 3: DONE");
        System.out.println("Вторая подзадача с id 6 к эпику 3: DONE");
        System.out.println("Результат: \n");


        fileManagerIn.updateTask(1, "IN_PROGRESS");
        fileManagerIn.updateSubTask(5, "DONE");
        fileManagerIn.updateSubTask(6, "DONE");
        printTask(fileManagerIn);

        System.out.println("\n");
        System.out.println("Показать подзадачи эпика 4"); // показать подзадачи эпика

        ArrayList<Subtask> sub = fileManagerIn.showSubtask(4);
        for (Subtask s : sub) {
            System.out.println(s);
        }
        System.out.println("\n");
        System.out.println("Показать историю вызовов");
        fileManagerIn.showTask(1);
        fileManagerIn.showTask(1);
        fileManagerIn.showEpic(3);
        fileManagerIn.showSubTask(5);
        fileManagerIn.showSubTask(6);
        fileManagerIn.showSubTask(7);
        fileManagerIn.showTask(2);
        fileManagerIn.showEpic(4);
        fileManagerIn.showEpic(3);
        fileManagerIn.showSubTask(5);
        fileManagerIn.showSubTask(6);
        fileManagerIn.showSubTask(7);
        fileManagerIn.showTask(2);

        for (Task hist : fileManagerIn.getHistory()) {
            System.out.println(hist);
        }

        System.out.println("Удаление из истории по id 5: ");
        fileManagerIn.remove(5);


        for (Task hist : fileManagerIn.getHistory()) {
            System.out.println(hist);
        }

        System.out.println("Удаление по id 2, 3, 7:");
        fileManagerIn.removeByIdTask(2);
        fileManagerIn.removeByIdEpic(3);
        fileManagerIn.removeByIdSubtask(7);
        printTask(fileManagerIn);


        // удалить все
        System.out.println("Удалить все задачи:");
        fileManagerIn.removeAllTask();
        fileManagerIn.removeAllEpic();
        fileManagerIn.removeAllSubTask();
        //  printTask();

    }

    public static void printTask(FileBackedTaskManager fileManagerIn) {
        System.out.println("\nСписок задач: ");
        for (Task task : fileManagerIn.getAllTask()) {
            System.out.println(task);
        }

        System.out.println("Список эпиков: ");
        for (Epic epic : fileManagerIn.getAllEpic()) {
            System.out.println(epic);
        }

        System.out.println("Список подзадач: ");
        for (Subtask subtask : fileManagerIn.getAllSubTask()) {
            System.out.println(subtask);
        }
    }
}


