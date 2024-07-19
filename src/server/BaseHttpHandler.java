package server;


import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.NotFoundException;
import exception.TimeConflictException;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


abstract class BaseHttpHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected final TaskManager taskManager;
    protected final Gson gson;

    public BaseHttpHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        String method = exchange.getRequestMethod();
        String[] splitPath = path.split("/");


        switch (method) {
            case "GET":
                processGet(exchange, splitPath);
                break;
            case "POST":
                processPost(exchange, splitPath, body);
                break;
            case "DELETE":
                processDelete(exchange, splitPath);
                break;
            default:
                sendNotFound(exchange, "405 METHOD_NOT_ALLOWED");

        }
    }

    protected void processGet(HttpExchange exchange, String[] splitPath) throws IOException {
        if (splitPath.length == 4) {
            getEpicSubtaskId(exchange, splitPath[2]);                        // 1. Получение подзадач эпика по id
        } else if (splitPath.length == 3) {
            getTaskById(exchange, splitPath[2]);                             // 2. Получение задачи по id
        } else if (splitPath.length == 2) {
            getAllTask(exchange);                                            // 3. Получение всех задач
        } else {
            sendNotFound(exchange, "405 METHOD_NOT_ALLOWED");
        }
    }

    protected void processPost(HttpExchange exchange, String[] splitPath, String body) throws IOException {
        if (splitPath.length == 2) {
            Task task = gson.fromJson(body, Task.class);
            if ((task.getId() == null) || (task.getId() == 0)) {
                createNewTask(body, exchange);                               // 4. Добавление задачи
            } else {
                updateTaskById(exchange, task.getId(), body);                // 5. Обновление задачи
            }
        } else {
            sendNotFound(exchange, "Путь не корректен");
        }
    }

    protected void processDelete(HttpExchange exchange, String[] splitPath) throws IOException {
        if (splitPath.length == 3) {
            deleteTaskById(exchange, splitPath[2]);                                        // 6. Удаление задачи по id
        } else {
            sendNotFound(exchange, "405 METHOD_NOT_ALLOWED");
        }
    }


    protected void getEpicSubtaskId(HttpExchange exchange, String id) throws IOException {    // # 1. Получение подзадач эпика по id
        try {
            String epicSubJson = gson.toJson(taskManager.showEpic(Integer.parseInt(id)));
            sendText(exchange, epicSubJson);
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Эпик не найден");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }


    protected void getTaskById(HttpExchange exchange, String id) throws IOException {        // # 2. Получение задачи по id
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

    protected void getAllTask(HttpExchange exchange) throws IOException {                       // # 3. Получение всех задач
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

    protected void createNewTask(String body, HttpExchange exchange) throws IOException {        // # 4. Создание задачи
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

    protected void deleteTaskById(HttpExchange exchange, String id) throws IOException {         // 1.4 Удаление задачи по id
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

    protected void updateTaskById(HttpExchange exchange, int id, String body) throws IOException {   //1.5 Обновление задачи по id
        try {
            Task task = gson.fromJson(body, Task.class);
            taskManager.updateTask(id, task);

            sendAdd(exchange, "Задача обновлена успешно");
        } catch (TimeConflictException e) {
            sendHasInteractions(exchange, "Данное время занято");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    protected void sendResponse(HttpExchange h, int statusCode, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(statusCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        sendResponse(h, 200, text);
    }

    protected void sendAdd(HttpExchange h, String text) throws IOException {
        sendResponse(h, 201, text);
    }

    public void sendNotFound(HttpExchange h, String text) throws IOException {
        sendResponse(h, 404, text);
    }

    public void sendHasInteractions(HttpExchange h, String text) throws IOException {
        sendResponse(h, 406, text);
    }

    public void sendInternalServerError(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(500, 0);
    }


}




