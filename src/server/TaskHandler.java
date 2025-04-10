package server;


import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import exception.TimeConflictException;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class TaskHandler extends BaseHttpHandler {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
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

    protected void createNewTask(String body, HttpExchange exchange) throws IOException {        // 1.2  Создание задачи
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

    @Override
    protected void getAllTask(HttpExchange exchange) throws IOException {                       // 3. Получение всех задач
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

    protected void getTaskById(HttpExchange exchange, String id) throws IOException {        // 1.3 Получение задачи по id
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

    protected void deleteTaskById(HttpExchange exchange, String id) throws IOException {         // 1.4 Удаление задачи по id
        try {
            taskManager.removeByIdTask(Integer.parseInt(id));
            try (OutputStream os = exchange.getResponseBody()) {
                exchange.sendResponseHeaders(201, 0);
            }
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

}



