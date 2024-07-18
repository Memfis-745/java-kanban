import exception.NotFoundException;
import server.HttpTaskServer;
import com.google.gson.Gson;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

import static java.net.http.HttpRequest.BodyPublishers;
import static java.net.http.HttpRequest.newBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testng.AssertJUnit.assertNull;
import static tasks.Status.NEW;

public class HttpTaskServerTest {

    Gson gson = Managers.getGson();
    TaskManager taskManager;
    HttpTaskServer taskServer;
    HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    void init() throws IOException {
        taskManager = Managers.getDefault();
        taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
    }

    @AfterEach
    void afterEach() {
        taskServer.stop();
    }

    @Test
    public void CreateTask() throws IOException, InterruptedException, NotFoundException {
        taskManager.nulId();
        Task expectedTask = new Task("Задача-1", "Описание задачи-1", Duration.ofMinutes(120), LocalDateTime.of(2024, 1, 1, 0, 0));
        expectedTask.setStatus(NEW);
        URI url = URI.create("http://localhost:8080/tasks");
        String body = gson.toJson(expectedTask);

        HttpRequest request = newBuilder().uri(url).POST(BodyPublishers.ofString(body, StandardCharsets.UTF_8)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        expectedTask.setId(1);

        assertEquals(201, response.statusCode(), "Код ответа не совпадает");
        assertEquals(expectedTask, taskManager.showTask(1), "Задачи не совпадают");

    }

    @Test
    public void CorrectReturnedTaskById() throws IOException, InterruptedException {
        taskManager.nulId();
        Task task = new Task("Задача-1", "Описание задачи-1", Duration.ofMinutes(120), LocalDateTime.of(2024, 1, 1, 0, 0));
        URI url = URI.create("http://localhost:8080/tasks/1");
        taskManager.addTask(task);
        String expectedResponse = gson.toJson(task);

        HttpRequest request = newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа не совпадает");

        assertEquals(expectedResponse, response.body());
    }

    @Test
    public void CorrectReturnListOfTasks() throws IOException, InterruptedException {
        taskManager.nulId();
        Task task1 = new Task("Задача-1", "Описание задачи-1", Duration.ofMinutes(120), LocalDateTime.of(2024, 1, 1, 0, 0));
        Task task2 = new Task("Задача-2", "Описание задачи-2", Duration.ofMinutes(120), LocalDateTime.of(2024, 2, 1, 0, 0));
        URI url = URI.create("http://localhost:8080/tasks");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        String expectedResponse = gson.toJson(taskManager.getAllTask());

        HttpRequest request = newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(expectedResponse, response.body());
    }


    @Test
    public void UpdateTask() throws IOException, InterruptedException, NotFoundException {
        taskManager.nulId();
        Task task1 = new Task("Задача-1", "Описание задачи-1", Duration.ofMinutes(120), LocalDateTime.of(2024, 1, 1, 0, 0));
        Task task2 = new Task("Задача-2", "Описание задачи-2", Duration.ofMinutes(120), LocalDateTime.of(2024, 2, 1, 0, 0));
        URI url = URI.create("http://localhost:8080/tasks");

        int idTask1 = taskManager.addTask(task1);
        task2.setId(idTask1);
        task2.setStatus(NEW);

        String body = gson.toJson(task2);

        HttpRequest request = newBuilder().uri(url).POST(BodyPublishers.ofString(body, StandardCharsets.UTF_8)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(task2, taskManager.showTask(1));
    }

    @Test
    public void CreateSubtask() throws IOException, InterruptedException, NotFoundException {
        taskManager.nulId();
        Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);

        Subtask expectedSubTask = new Subtask("подзадача-5", "первая подзадача к эпику 3", epic.getId(), Duration.ofMinutes(103), LocalDateTime.of(2024, 1, 1, 0, 0));
        expectedSubTask.setStatus(NEW);
        System.out.println("Эпик из теста " + epic.getId());
        URI url = URI.create("http://localhost:8080/subtasks");
        String body = gson.toJson(expectedSubTask);

        HttpRequest request = newBuilder().uri(url).POST(BodyPublishers.ofString(body, StandardCharsets.UTF_8)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        expectedSubTask.setId(2);

        assertEquals(expectedSubTask, taskManager.showSubTask(2));
    }

    @Test
    public void UpdateSubtask() throws IOException, InterruptedException, NotFoundException {
        taskManager.nulId();
        Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);
        Subtask subtaskCreate = new Subtask("подзадача", "первая подзадача к эпику", epic.getId(), Duration.ofMinutes(103), LocalDateTime.of(2024, 1, 1, 0, 0));
        Subtask subtaskUpd = new Subtask("подзадача", "обновленная подзадача к эпику", epic.getId(), Duration.ofMinutes(103), LocalDateTime.of(2024, 2, 1, 0, 0));
        subtaskUpd.setStatus(NEW);
        taskManager.addSubTask(subtaskCreate);
        subtaskUpd.setId(2);

        URI url = URI.create("http://localhost:8080/subtasks");
        String body = gson.toJson(subtaskUpd);

        HttpRequest request = newBuilder().uri(url).POST(BodyPublishers.ofString(body, StandardCharsets.UTF_8)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(subtaskUpd, taskManager.showSubTask(2));
    }

    @Test
    public void CorrectReturnSubtaskById() throws IOException, InterruptedException, NotFoundException {
        taskManager.nulId();
        Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("подзадача-5", "первая подзадача к эпику 3", epic.getId(), Duration.ofMinutes(103), LocalDateTime.of(2024, 1, 1, 0, 0));
        taskManager.addSubTask(subtask);

        URI url = URI.create("http://localhost:8080/subtasks/2");
        String expected = gson.toJson(subtask);

        HttpRequest request = newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(expected, response.body());

    }

    @Test
    public void CorrectReturnSubtasksMap() throws IOException, InterruptedException, NotFoundException {
        taskManager.nulId();
        Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("подзадача-5", "первая подзадача к эпику 3", epic.getId(), Duration.ofMinutes(103), LocalDateTime.of(2024, 1, 1, 0, 0));
        Subtask subtask2 = new Subtask("подзадача-5", "первая подзадача к эпику 3", epic.getId(), Duration.ofMinutes(103), LocalDateTime.of(2024, 1, 1, 0, 0));
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);

        URI url = URI.create("http://localhost:8080/subtasks");
        String expected = gson.toJson(taskManager.getAllSubTask());

        HttpRequest request = newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(expected, response.body());
    }

    @Test
    public void CreateOfEpic() throws IOException, InterruptedException, NotFoundException {
        taskManager.nulId();
        Epic expectedEpic = new Epic("Epic", "Description");
        URI url = URI.create("http://localhost:8080/epics");
        String body = gson.toJson(expectedEpic);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = newBuilder().uri(url).method("POST", BodyPublishers.ofString(body)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        expectedEpic.setId(1);
        expectedEpic.setStatus(NEW);

        assertEquals(expectedEpic, taskManager.showEpic(1));
    }

    @Test
    public void CorrectReturnEpicById() throws IOException, InterruptedException {
        taskManager.nulId();
        Epic expectedEpic = new Epic("Epic", "Description");
        URI url = URI.create("http://localhost:8080/epics/1");
        taskManager.addEpic(expectedEpic);
        String expectedResponse = gson.toJson(expectedEpic);

        HttpRequest request = newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(expectedResponse, response.body());
    }

    @Test
    public void CorrectReturnEpicsMap() throws IOException, InterruptedException {
        taskManager.nulId();
        Epic epic1 = new Epic("Epic1", "Description1");
        Epic epic2 = new Epic("Epic2", "Description2");
        URI url = URI.create("http://localhost:8080/epics");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        String expectedResponse = gson.toJson(taskManager.getAllEpic());

        HttpRequest request = newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(expectedResponse, response.body());
    }

    @Test
    public void BrokenRequestMethod() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks");

        HttpRequest request = newBuilder().uri(url).PUT(BodyPublishers.ofString("Test")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());

    }

    @Test
    public void RemoveTasksById() throws IOException, InterruptedException {
        taskManager.nulId();
        Task task = new Task("Задача-1", "Описание задачи-1", Duration.ofMinutes(120), LocalDateTime.of(2024, 1, 1, 0, 0));
        taskManager.addTask(task);

        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(url).method("DELETE", HttpRequest.BodyPublishers.ofString("tasks")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Код ответа не совпадает");
        assertNull(taskManager.showTask(task.getId()));
        System.out.println("ответ");

    }

}