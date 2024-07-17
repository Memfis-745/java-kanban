package Server;

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
        httpServer.createContext("/subtasks", new TaskHandler(taskManager, gson));
        httpServer.createContext("/epics", new TaskHandler(taskManager, gson));
        httpServer.createContext("/history", new TaskHandler(taskManager, gson));
        httpServer.createContext("/prioritized", new TaskHandler(taskManager, gson));

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







/*

if (taskManager.showTask(idInt) != null) {
                if (body.contains(NEW.toString())) {
                    taskManager.updateTask(idInt, NEW.toString());
                } else if (body.contains(IN_PROGRESS.toString())) {
                    taskManager.updateTask(idInt, IN_PROGRESS.toString());
                } else if (body.contains(DONE.toString())) {
                    taskManager.updateTask(idInt, DONE.toString());
                }
            } else {
                writeResponse(exchange, "Задача не найдена", 404);
                return;
            }


        // IOException могут сгенерировать методы create() и bind(...)
        public static void main(String[] args) throws IOException {
            HttpServer httpServer = HttpServer.create();

            httpServer.bind(new InetSocketAddress(PORT), 0); // связываем сервер с сетевым портом
            httpServer.createContext("/hello", new HelloHandler()); // связываем путь и обработчик
            httpServer.start(); // запускаем сервер

            System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        }


        static class HelloHandler implements HttpHandler {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                System.out.println("Началась обработка /hello запроса от клиента.");

                String response = "Hey! Glad to see you on our server.";
                httpExchange.sendResponseHeaders(200, 0);

                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }

 */
        /*HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0); // связываем сервер с сетевым портом
        httpServer.createContext("/hello", new HelloHandler());
        HttpTaskServer httpTaskServer = new HttpTaskServer.start();


this.httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", this::handle);
         */




 /*HttpServer httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0); // связываем сервер с сетевым портом
        httpServer.createContext("/hello", new HelloHandler()); // связываем путь и обработчик
        httpServer.start(); // запускаем сервер


  */