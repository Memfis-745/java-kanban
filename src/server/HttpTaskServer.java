package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import managers.InMemoryTaskManager;
import managers.Managers;
import managers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;


    private TaskManager taskManager;
    private HttpServer httpServer;
    private Gson gson;

    public HttpTaskServer(TaskManager taskManager) throws IOException {

        this.taskManager = taskManager;
        gson = Managers.getGson();
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager, gson)); // связываем путь и обработчик
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager, gson));
        httpServer.createContext("/epics", new EpicHandler(taskManager, gson));
        httpServer.createContext("/history", new HystoryHandler(taskManager, gson));
        httpServer.createContext("/prioritized", new PrioritizeHandler(taskManager, gson));

    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(new InMemoryTaskManager());


        httpTaskServer.start(); // запускаем сервер
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");

    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }


}


