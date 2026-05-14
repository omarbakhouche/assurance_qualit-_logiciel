package com.tp3.taskmanager;

import com.tp3.AbstractIntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the Task Manager using Testcontainers and MySQL.
 * 
 * This suite improves upon the original H2-based tests by using a production-equivalent 
 * MySQL container, ensuring strict constraint validation and environment parity.
 * Each test is isolated via @Transactional rollbacks.
 */
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskManagerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    // --- Helper ---

    private Task createAndSaveTask(String title, String description) {
        return taskService.saveTask(new Task(title, description));
    }

    // --- Tests ---

    @Test
    @Order(1)
    @DisplayName("Should persist a new task to MySQL and assign a unique ID")
    void testCreateTask() {
        Task task = createAndSaveTask("Tâche 1", "Description de la tâche 1");

        assertNotNull(task.getId(), "Task ID should be generated upon persistence");
        assertEquals("Tâche 1", task.getTitle());
        assertEquals("Description de la tâche 1", task.getDescription());
        assertFalse(task.isCompleted(), "New tasks must default to an uncompleted state");

        assertTrue(taskRepository.existsById(task.getId()));
    }

    @Test
    @Order(2)
    @DisplayName("Should retrieve a previously saved task by its ID")
    void testGetTask() {
        Task saved = createAndSaveTask("Tâche 2", "Description de la tâche 2");

        Optional<Task> retrieved = taskService.findTaskById(saved.getId());

        assertTrue(retrieved.isPresent());
        assertEquals(saved.getId(), retrieved.get().getId());
        assertEquals("Tâche 2", retrieved.get().getTitle());
        assertEquals("Description de la tâche 2", retrieved.get().getDescription());
    }

    @Test
    @Order(3)
    @DisplayName("Should remove a task from the database by ID")
    void testDeleteTask() {
        Task saved = createAndSaveTask("Tâche à supprimer", "Desc");
        Long id = saved.getId();

        taskService.deleteTaskById(id);

        Optional<Task> retrieved = taskService.findTaskById(id);
        assertFalse(retrieved.isPresent(), "The task should not exist after deletion");
    }

    @Test
    @Order(4)
    @DisplayName("Should update completion status and persist changes in MySQL")
    void testCompleteTask() {
        Task saved = createAndSaveTask("Tâche à terminer", "Desc");
        assertFalse(saved.isCompleted());

        Task completed = taskService.completeTask(saved.getId());

        assertTrue(completed.isCompleted());
        Optional<Task> fromDb = taskService.findTaskById(saved.getId());
        assertTrue(fromDb.isPresent());
        assertTrue(fromDb.get().isCompleted());
    }

    @Test
    @Order(5)
    @DisplayName("Should filter and return only pending tasks")
    void testFindPendingTasks() {
        Task t1 = createAndSaveTask("Pending 1", "");
        Task t2 = createAndSaveTask("Pending 2", "");
        Task t3 = createAndSaveTask("Done", "");
        taskService.completeTask(t3.getId());

        List<Task> pending = taskService.findPendingTasks();

        List<Long> pendingIds = pending.stream().map(Task::getId).toList();
        assertTrue(pendingIds.contains(t1.getId()));
        assertTrue(pendingIds.contains(t2.getId()));
        assertFalse(pendingIds.contains(t3.getId()));
    }

    @Test
    @Order(6)
    @DisplayName("Should filter and return only completed tasks")
    void testFindCompletedTasks() {
        Task t1 = createAndSaveTask("Will be done", "");
        Task t2 = createAndSaveTask("Still pending", "");
        taskService.completeTask(t1.getId());

        List<Task> completed = taskService.findCompletedTasks();

        List<Long> completedIds = completed.stream().map(Task::getId).toList();
        assertTrue(completedIds.contains(t1.getId()));
        assertFalse(completedIds.contains(t2.getId()));
    }

    @Test
    @Order(7)
    @DisplayName("Should return an empty Optional when searching for a non-existent task")
    void testGetTask_NotFound() {
        Optional<Task> result = taskService.findTaskById(999_999L);
        assertTrue(result.isEmpty());
    }

    @Test
    @Order(8)
    @DisplayName("Should validate task title and throw exception if blank")
    void testCreateTask_BlankTitle_Throws() {
        assertThrows(IllegalArgumentException.class,
            () -> taskService.saveTask(new Task("", "desc")));
    }

    @Test
    @Order(9)
    @DisplayName("Should throw TaskNotFoundException when deleting a non-existent ID")
    void testDeleteTask_NotFound_Throws() {
        assertThrows(
            TaskService.TaskNotFoundException.class,
            () -> taskService.deleteTaskById(999_999L)
        );
    }

    @Test
    @Order(10)
    @DisplayName("Should throw TaskNotFoundException when completing a non-existent ID")
    void testCompleteTask_NotFound_Throws() {
        assertThrows(
            TaskService.TaskNotFoundException.class,
            () -> taskService.completeTask(999_999L)
        );
    }
}