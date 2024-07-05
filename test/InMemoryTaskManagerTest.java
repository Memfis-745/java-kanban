import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import tasks.Status;
import tasks.*;
import managers.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    //    static final InMemoryTaskManager taskManager = (InMemoryTaskManager) Managers.getDefault();
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
