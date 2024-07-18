package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import exception.TimeConflictException;
import managers.TaskManager;
import tasks.Subtask;

import java.io.IOException;

public class SubtaskHandler extends BaseHttpHandler {
    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    protected void processPost(HttpExchange exchange, String[] splitPath, String body) throws IOException {
        if (splitPath.length == 2) {
            Subtask subtask = gson.fromJson(body, Subtask.class);
            if ((subtask.getId() == null) || (subtask.getId() == 0)) {
                createNewTask(body, exchange);                               // 4. Добавление задачи
            } else {
                updateTaskById(exchange, subtask.getId(), body);                 // 5. Обновление задачи
            }
        } else {
            sendNotFound(exchange, "Путь не корректен");
        }
    }

    @Override
    protected void createNewTask(String body, HttpExchange exchange) throws IOException {        // 2.5  Создание эпика
        try {
            Subtask subtask = gson.fromJson(body, Subtask.class);
            taskManager.addSubTask(subtask);
            sendAdd(exchange, "Подзадача создана");
        } catch (TimeConflictException exception) {
            sendHasInteractions(exchange, "Данное время занято");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    @Override
    protected void updateTaskById(HttpExchange exchange, int id, String body) throws IOException {   //3.3 Обновление задачи по id
        try {

            Subtask subtask = gson.fromJson(body, Subtask.class);
            taskManager.updateSubTask(id, subtask);
            sendAdd(exchange, "Подзадача обновлена");
        } catch (TimeConflictException e) {
            sendHasInteractions(exchange, "Данное время занято");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    @Override
    protected void getTaskById(HttpExchange exchange, String id) throws IOException {        // # 2. Получение задачи по id
        try {
            Subtask subtask = taskManager.showSubTask(Integer.parseInt(id));
            String taskJson = gson.toJson(subtask);
            sendText(exchange, taskJson);
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Задача не найдена");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    @Override
    protected void getAllTask(HttpExchange exchange) throws IOException {                       // # 3. Получение всех задач
        try {
            if (!taskManager.getAllSubTask().isEmpty()) {
                String taskJson = gson.toJson(taskManager.getAllSubTask());
                sendText(exchange, taskJson);
            } else {
                sendNotFound(exchange, "Список задач пуст");
            }
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    @Override
    protected void deleteTaskById(HttpExchange exchange, String id) throws IOException {         // 1.4 Удаление задачи по id
        try {
            taskManager.removeByIdSubtask(Integer.parseInt(id));
            exchange.sendResponseHeaders(201, 0);
            sendAdd(exchange, "Здача удалена: ");
        } catch (NotFoundException exception) {
            sendNotFound(exchange, "Задача не найдена");
        } catch (Exception exception) {
            sendInternalServerError(exchange);
        }
    }

}