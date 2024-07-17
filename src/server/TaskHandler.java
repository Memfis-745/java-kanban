package server;


import exception.NotFoundException;
import exception.TimeConflictException;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class TaskHandler extends BaseHttpHandler {

    // Gson gson;
    //  TaskManager taskManager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        String method = exchange.getRequestMethod();
        String[] splitPath = path.split("/");
        int responseCode;
        String response;

        switch (splitPath[1]) {
            case "tasks":
                taskHandler(splitPath, body, method, exchange);
                break;
            case "epics":
                epicHandler(splitPath, body, method, exchange);
                break;
            case "subtasks":
                subtaskHandler(splitPath, body, method, exchange);
                break;
            case "history":
                historyHandler(splitPath, body, method, exchange);
                break;
            case "prioritized":
                prioritizedHandler(splitPath, body, method, exchange);
                break;
            default:
                response = "Такого эндпоинта не существует";
                responseCode = 404;
                sendNotFound(exchange, response + responseCode);

        }

    }


    public void taskHandler(String[] path, String body, String method, HttpExchange exchange) throws IOException {
        if (path.length == 3) {
            if (method.equals("GET")) {
                getTaskById(exchange, path[2]);             // 1.3 Получение задачи по id
            } else if (method.equals("DELETE")) {
                deleteTaskById(exchange, path[2]);          // 1.4 Удаление задачи по id
            } else if (method.equals("POST")) {
                updateTaskById(exchange, path[2], body);     // 1.5 Обновление задачи по id
            } else {
                System.out.println("Такого метода нет");
            }
        } else if (path.length == 2) {
            if (method.equals("GET")) {                       // 1.1 Получение всех задач
                getTasks(exchange);
            } else if (method.equals("POST")) {
                createNewTask(body, exchange);                 // 1.2 Создание задачи
            } else {
                sendNotFound(exchange, "Такого метода нет");
            }
        } else {
            sendNotFound(exchange, "Путь не корректен");
        }
    }

    public void epicHandler(String[] path, String body, String method, HttpExchange exchange) throws IOException {
        if (path.length == 4) {
            getEpicSubtaskId(exchange, path[2]);            // 2.1 Получение подзадач по id эпика
        }
        if (path.length == 3) {
            if (method.equals("GET")) {
                getEpicById(exchange, path[2]);                  //2.2 Получение эпика по id
            } else if (method.equals("DELETE")) {
                deliteEpicId(exchange, path[2]);                 // 2.3 Удаление эпика по id
            } else {
                System.out.println("Такого метода нет");
            }
        } else if (path.length == 2) {
            if (method.equals("GET")) {                            //2.4 Получение всех эпиков
                getAllEpics(exchange);
            } else if (method.equals("POST")) {
                createEpic(body, exchange);                         //2.5 Создание нового эпика
            } else {
                System.out.println("Такого метода нет");
            }
        } else {
            sendNotFound(exchange, "Путь не корректен");
        }
    }

    public void subtaskHandler(String[] path, String body, String method, HttpExchange exchange) throws IOException {
        if (path.length == 3) {
            if (method.equals("GET")) {                             //3.1 Получение подзадачи по id
                getSubtaskById(exchange, path[2]);
            } else if (method.equals("DELETE")) {                   //3.2 Удаление подзадачи по id
                deliteSubtaskById(exchange, path[2]);
            } else if (method.equals("POST")) {                     //3.3 Обновление подзадачи по id
                updateSubtaskById(exchange, path[2], body);
            }
        } else if (path.length == 2) {
            if (method.equals("GET")) {                             //3.4 Получение всех подзадач
                getSubtasks(exchange);
            } else if (method.equals("POST")) {
                createNewSubtask(body, exchange);                    //3.5 Создание новой подзадачи
            } else {
                System.out.println("Такого метода нет");
            }
        } else {
            sendNotFound(exchange, "Путь не корректен");
        }
    }


    private void createNewTask(String body, HttpExchange exchange) throws IOException {        // 1.2  Создание задачи
        try {
            Task task = gson.fromJson(body, Task.class);
            int newTask = taskManager.addTask(task);
            String taskJson = gson.toJson(newTask);
            sendAdd(exchange, "Здача создана с id: " + taskJson);
        } catch (TimeConflictException exception) {
            sendHasInteractions(exchange, "Задача с таким интервалом существует");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    private void getTasks(HttpExchange exchange) throws IOException {                       // 1.1 Получение всех задач
        try {
            if (!taskManager.getAllTask().isEmpty()) {
                String taskJson = gson.toJson(taskManager.getAllTask());
                sendText(exchange, taskJson);
            } else {
                sendNotFound(exchange, "Список задач пуст");
            }
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    private void getTaskById(HttpExchange exchange, String id) throws IOException {        // 1.3 Получение задачи по id
        try {
            Task task = taskManager.showTask(Integer.parseInt(id));
            String taskJson = gson.toJson(task);
            sendText(exchange, taskJson);
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Задача не найдена");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    private void deleteTaskById(HttpExchange exchange, String id) throws IOException {         // 1.4 Удаление задачи по id
        try {
            taskManager.removeByIdTask(Integer.parseInt(id));
            exchange.sendResponseHeaders(201, 0);
            sendAdd(exchange, "Здача удалена: ");
        } catch (NotFoundException exception) {
            sendNotFound(exchange, "Задача не найдена");
        } catch (Exception exception) {
            sendInternalServerError(exchange);
        }
    }


    private void updateTaskById(HttpExchange exchange, String id, String body) throws IOException {   //1.5 Обновление задачи по id
        try {
            int idInt = Integer.parseInt(id);
            Task task = gson.fromJson(body, Task.class);
            taskManager.updateTask(idInt, task);

            sendAdd(exchange, "Задача обновлена успешно");
        } catch (TimeConflictException e) {
            sendHasInteractions(exchange, "Данное время занято");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    private void getEpicSubtaskId(HttpExchange exchange, String id) throws IOException {    //2.1 Получение подзадач эпика по id
        try {
            String EpicSubJson = gson.toJson(taskManager.showEpic(Integer.parseInt(id)));
            sendText(exchange, EpicSubJson);
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Эпик не найден");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    private void getEpicById(HttpExchange exchange, String id) throws IOException {     // 2.2 Получение эпика по id
        try {
            Epic epic = taskManager.showEpic(Integer.parseInt(id));
            String epiJson = gson.toJson(epic);
            sendText(exchange, epiJson);

        } catch (NotFoundException e) {
            sendNotFound(exchange, "Эпик не найден");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    private void deliteEpicId(HttpExchange exchange, String id) throws IOException {         // 2.3 Удаление эпика по id
        try {
            taskManager.removeByIdEpic(Integer.parseInt(id));
            exchange.sendResponseHeaders(200, 0);
        } catch (NotFoundException exception) {
            sendNotFound(exchange, "Эпик не найден");
        } catch (Exception exception) {
            sendInternalServerError(exchange);
        }
    }

    private void getAllEpics(HttpExchange exchange) throws IOException {                    // 2.4 Получение всех эпиков
        try {

            String taskJson = gson.toJson(taskManager.getAllEpic());
            sendText(exchange, taskJson);
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    private void createEpic(String body, HttpExchange exchange) throws IOException {        // 2.5  Создание эпика
        try {
            Epic epic = gson.fromJson(body, Epic.class);
            taskManager.addEpic(epic);
            sendAdd(exchange, "Эпик создан");
        } catch (TimeConflictException exception) {
            sendHasInteractions(exchange, "Данное время занято");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }


    private void getSubtaskById(HttpExchange exchange, String id) throws IOException {    //3.1 Получение подзадачи по id
        try {
            Subtask subtask = taskManager.showSubTask(Integer.parseInt(id));
            String taskJson = gson.toJson(subtask);
            sendText(exchange, taskJson);
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Подзадача не найдена");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    private void deliteSubtaskById(HttpExchange exchange, String id) throws IOException {    //3.2 Удаление подзадачи по id
        try {
            taskManager.removeByIdSubtask(Integer.parseInt(id));
            exchange.sendResponseHeaders(200, 0);
        } catch (NotFoundException exception) {
            sendNotFound(exchange, "Подзадача не найдена");
        } catch (Exception exception) {
            sendInternalServerError(exchange);
        }
    }

    private void updateSubtaskById(HttpExchange exchange, String id, String body) throws IOException {   //3.3 Обновление задачи по id
        try {

            int idInt = Integer.parseInt(id);
            Subtask subtask = gson.fromJson(body, Subtask.class);
            taskManager.updateSubTask(idInt, subtask);
            sendAdd(exchange, "Подзадача обновлена");
        } catch (TimeConflictException e) {
            sendHasInteractions(exchange, "Данное время занято");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }


    private void getSubtasks(HttpExchange exchange) throws IOException {                       // 3.4 Получение всех подзадач
        try {
            String taskJson = gson.toJson(taskManager.getAllSubTask());
            sendText(exchange, taskJson);
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    private void createNewSubtask(String body, HttpExchange exchange) throws IOException {   // 3.5 Создание новой подзадачи
        try {
            Subtask subtask = gson.fromJson(body, Subtask.class);
            int newSubTask = taskManager.addSubTask(subtask);
            String taskJson = gson.toJson(newSubTask);
            sendAdd(exchange, "Подзадача создана");
        } catch (TimeConflictException exception) {
            sendHasInteractions(exchange, "Данное время занято");
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Эпик не найден");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    private void historyHandler(String[] splitPath, String body, String method, HttpExchange exchange) throws IOException {
        try {
            String HistoryToJson = gson.toJson(taskManager.getHistory());
            sendText(exchange, HistoryToJson);
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }

    }

    private void prioritizedHandler(String[] splitPath, String body, String method, HttpExchange exchange) throws IOException {
        try {
            String prioritaizerToJson = gson.toJson(taskManager.getPrioritizedTasks());
            sendText(exchange, prioritaizerToJson);
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

}





/*
    private void updateTaskById(HttpExchange exchange, String id, String body) throws IOException {   //1.5 Обновление задачи по id
        try {
            int idInt = Integer.parseInt(id);
            Task task = gson.fromJson(body, Task.class);
            sendAdd(exchange, "Задача обновлена успешно");
        } catch (TimeConflictException e) {
            sendHasInteractions(exchange, "Данное время занято");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
            /*if (task == null) {
                writeResponse(exchange, "Входящая подзадача пуста", 404);
            }
            int kod = taskManager.updateTask(idInt,task);
            if(kod==404){
                writeResponse(exchange, "Подзадача не найдена", kod);
                return;
            } else if (kod == 406){
                writeResponse(exchange, "Подзадачи пересекаются по времени", kod);
            } else if (kod == 201) {
                String taskJson = "Задача успешно обновлена";
                writeResponse(exchange, taskJson + id, kod);
            }
        } catch (JsonSyntaxException e) {
            try {
                writeResponse(exchange, "Получен некорреткный JSON", 400);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

     private void updateSubtaskById(HttpExchange exchange, String id, String body) throws IOException{   //3.3 Обновление задачи по id
        try {

            int idInt = Integer.parseInt(id);
            Subtask subtask = gson.fromJson(body, Subtask.class);
            if (subtask == null) {
                writeResponse(exchange, "Входящая подзадача пуста", 404);
            }
            int kod = taskManager.updateSubTask(idInt, subtask);
            if(kod==404){
                writeResponse(exchange, "Подзадача не найдена", kod);
                return;
            } else if (kod == 406){
                writeResponse(exchange, "Подзадачи пересекаются по времени", kod);
            } else if (kod == 201) {
                String taskJson = "Подзадача успешно обновлена";
                writeResponse(exchange, taskJson + id, kod);
            }
    */
