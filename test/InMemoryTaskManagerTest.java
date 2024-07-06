


import org.junit.jupiter.api.BeforeEach;

import tasks.*;
import managers.*;

import java.io.File;
import java.io.IOException;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    String name;
    String description; //

    int id;
    Task task = new Task("Test addNewTask", "Test addNewTask description");
    File file = new File("taskFile.csv");
    Status status = task.getStatus();

    void initial() {
        taskManager = new FileBackedTaskManager(file);
    }

    @BeforeEach
    void beforeEach() throws IOException {
        initial();
    }

}
