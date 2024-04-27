import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static final TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {


            boolean mainm = true;
            while (mainm) {
                printMenu();
                Scanner scanner = new Scanner(System.in);
                int index = scanner.nextInt();

                switch (index) {
                    case 1:
                        printTask();
                        break;
                    case 2:
                        taskManager.removeAllTask();
                        taskManager.removeAllEpic();
                        taskManager.removeAllSubTask();

                        break;
                    case 3:
                        System.out.println("Введите номер задачи:");
                        int id = scanner.nextInt();
                        Task task = taskManager.showTask(id);
                        System.out.println(task);

                        System.out.println("Введите номер epica:");
                        int idEpic = scanner.nextInt();
                        Epic epic = taskManager.showEpic(idEpic);
                        System.out.println(epic);

                        System.out.println("Введите номер подзадачи:");
                        int idSubTask = scanner.nextInt();
                        Subtask subtask = taskManager.showSubTask(idSubTask);
                        System.out.println(subtask);
                        break;
                    case 4:
                        System.out.println("Ввести простую задачу - 1");
                        System.out.println("Ввести эпик - 2");
                        System.out.println("Ввести подзадачу эпика - 3");
                        int in = scanner.nextInt();
                        scanner.nextLine();
                        if (in == 1) {              // ввод задач

                            System.out.println("Введите название задачи");
                            String name = scanner.nextLine();
                            System.out.println("Введите описание задачи");
                            String description = scanner.nextLine();
                            task = new Task(name, description);
                            taskManager.addTask(task);

                        } else if (in == 2) {  // ввод эпика
                            System.out.println("Введите название эпика");
                            String name = scanner.nextLine();
                            System.out.println("Введите его описание");
                            String description = scanner.nextLine();
                            epic = new Epic(name, description);
                            taskManager.addEpic(epic);

                        } else if (in == 3) {
                            boolean sub = true;

                            while (sub) {        // ввод подзадачи
                                System.out.println("Введите id эпика");
                                int epicId = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Введите название задачи");
                                String subName = scanner.nextLine();
                                System.out.println("Введите описание задачи");
                                String subDescription = scanner.nextLine();
                                subtask = new Subtask(subName, subDescription, epicId);
                                taskManager.addSubTask(subtask);

                                System.out.println("Добавить еще одну подзадачу? 1 - да, 2 - нет.");
                                int subIndex = scanner.nextInt();
                                scanner.nextLine();
                                if (subIndex == 2) {
                                    sub = false;
                                } else if (subIndex != 1) {
                                    System.out.println("Ошибочный ввод");
                                }
                            }
                        }
                        break;
                    case 5:      // смена статуса
                        System.out.println("1 - сменить статус задачи, 2 - изменить статус подзадачи:");
                        int ind = scanner.nextInt();
                        scanner.nextLine();
                        if (ind == 1) {
                            System.out.println("Введите номер задачи:");
                            int idStatus = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println("Введите статус задачи:");
                            String status = scanner.nextLine();
                            taskManager.updateTask(idStatus, status);
                            break;
                        } else if (ind == 2) {
                            System.out.println("Введите номер подзадачи:");
                            int idStatus = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println("Введите статус подзадачи:");
                            String status = scanner.nextLine();
                            taskManager.updateSubTask(idStatus, status);
                        }
                        break;
                    case 6:     // удаление по id
                        System.out.println("удалить: 1 - задачу, 2 - эпик 3- подзадачу:");
                        ind = scanner.nextInt();
                        scanner.nextLine();
                        if (ind == 1) {
                            System.out.println("Введите id задачи:");
                            int idRemove = scanner.nextInt();
                            taskManager.removeByIdTask(idRemove);
                        } else if (ind == 2) {
                            System.out.println("Введите id эпика:");
                            int idRemove = scanner.nextInt();
                            taskManager.removeByIdEpic(idRemove);
                        } else if (ind == 3) {
                            System.out.println("Введите id подзадачи:");
                            int idRemove = scanner.nextInt();
                            taskManager.removeByIdSubtask(idRemove);
                        }
                        break;
                    case 7:
                        System.out.println("Введите id эпика"); // показать подзадачи эпика
                        int idShowSub = scanner.nextInt();
                        ArrayList<Subtask> sub = taskManager.showSubtask(idShowSub);
                        for (Subtask s : sub) {
                            System.out.println(s);
                        }
                        break;
                    default:
                        System.out.println("Введено ошибочное значение.");
                        mainm = false;
                        break;
                }
            }

        }

        public static void printMenu () {
            System.out.println("Выберите пункт меню:");
            System.out.println("1. Вывести список всех задач");
            System.out.println("2. Удалить все задачи");
            System.out.println("3. Открыть задачу по идентификатору");
            System.out.println("4. Создать задачу");
            System.out.println("5. Обновить статус задачи");
            System.out.println("6. Удалить задачу по идентификатору");
            System.out.println("7. Вывести список всех подзадач эпика");

        }

        public static void printTask () {
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


