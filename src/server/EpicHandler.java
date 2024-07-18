package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import exception.TimeConflictException;
import managers.TaskManager;
import tasks.Epic;

import java.io.IOException;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    protected void processPost(HttpExchange exchange, String[] splitPath, String body) throws IOException {
        if (splitPath.length == 2) {
            Epic epic = gson.fromJson(body, Epic.class);
            if ((epic.getId() == null) || (epic.getId() == 0)) {
                createNewTask(body, exchange);                               // 4. Добавление задачи
            } else {
                sendNotFound(exchange, "Путь не корректен");                // 5. Обновление задачи
            }
        } else {
            sendNotFound(exchange, "Путь не корректен");
        }
    }

    @Override
    protected void createNewTask(String body, HttpExchange exchange) throws IOException {        // 2.5  Создание эпика
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

    @Override
    protected void getAllTask(HttpExchange exchange) throws IOException {                       // # 3. Получение всех задач
        try {
            if (!taskManager.getAllEpic().isEmpty()) {
                String taskJson = gson.toJson(taskManager.getAllEpic());
                sendText(exchange, taskJson);
            } else {
                sendNotFound(exchange, "Список задач пуст");
            }
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    @Override
    protected void getTaskById(HttpExchange exchange, String id) throws IOException {        // # 2. Получение задачи по id
        try {
            Epic epic = taskManager.showEpic(Integer.parseInt(id));
            String taskJson = gson.toJson(epic);
            sendText(exchange, taskJson);
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Задача не найдена");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    protected void getEpicSubtaskId(HttpExchange exchange, String id) throws IOException {    //2.1 Получение подзадач эпика по id
        try {
            String epicSubJson = gson.toJson(taskManager.showEpic(Integer.parseInt(id)));
            sendText(exchange, epicSubJson);
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Эпик не найден");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    @Override
    protected void deleteTaskById(HttpExchange exchange, String id) throws IOException {         // 1.4 Удаление задачи по id
        try {
            taskManager.removeByIdEpic(Integer.parseInt(id));
            exchange.sendResponseHeaders(201, 0);
            sendAdd(exchange, "Здача удалена: ");
        } catch (NotFoundException exception) {
            sendNotFound(exchange, "Эпик не найден");
        } catch (Exception exception) {
            sendInternalServerError(exchange);
        }
    }
}

