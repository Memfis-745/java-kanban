package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;

import java.io.IOException;

public class HystoryHandler extends BaseHttpHandler {
    public HystoryHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    protected void getAllTask(HttpExchange exchange) throws IOException {                       // # 3. Получение всех задач
        try {
            if (!taskManager.getHistory().isEmpty()) {
                String historyToJson = gson.toJson(taskManager.getHistory());
                sendText(exchange, historyToJson);
            } else {
                sendNotFound(exchange, "Список задач пуст");
            }
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

}
