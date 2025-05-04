
import org.example.model.Task;
import org.example.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class TaskControllerIntegrationTest {

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("root")
            .withPassword("password");

    @Autowired
    private TaskService taskService;

    @Test
    public void testCreateTask() {
        Task task = new Task("Task 1", "Description of Task 1");
        Task savedTask = taskService.saveTask(task);

        Long taskId = savedTask.getId();
        Optional<Task> retrievedTask = taskService.findTaskById(taskId);

        assertTrue(retrievedTask.isPresent());
        assertEquals(task.getTitle(), retrievedTask.get().getTitle());
        assertEquals(task.getDescription(), retrievedTask.get().getDescription());
    }

    @Test
    public void testGetTask() {
        Task task = createAndSaveTask();
        Long taskId = task.getId();
        Optional<Task> retrievedTask = taskService.findTaskById(taskId);

        assertTrue(retrievedTask.isPresent());
        assertEquals(task.getTitle(), retrievedTask.get().getTitle());
        assertEquals(task.getDescription(), retrievedTask.get().getDescription());
    }

    @Test
    public void testDeleteTask() {
        Task task = createAndSaveTask();
        Long taskId = task.getId();
        taskService.deleteTaskById(taskId);

        Optional<Task> retrievedTask = taskService.findTaskById(taskId);
        assertFalse(retrievedTask.isPresent());
    }

    private Task createAndSaveTask() {
        Task task = new Task("Task 1", "Description of Task 1");
        return taskService.saveTask(task);
    }

    // Dynamically configure the datasource to use Testcontainers MySQL
    static {
        mysqlContainer.start();
        System.setProperty("spring.datasource.url", mysqlContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysqlContainer.getUsername());
        System.setProperty("spring.datasource.password", mysqlContainer.getPassword());
    }
}
