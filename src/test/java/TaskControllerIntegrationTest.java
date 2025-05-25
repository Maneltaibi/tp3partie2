package org.example;

import org.example.model.Task;
import org.example.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Tp3partie2Application.class)
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.30")
            .withDatabaseName("taskdb")
            .withUsername("devuser")
            .withPassword("devpass");

    @DynamicPropertySource
    static void setProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    private TaskService taskService; // ✅ CORRIGÉ : ne pas appeler statiquement

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
}
