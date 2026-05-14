package com.tp3.exo1;

import com.tp3.AbstractIntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for UserService using Testcontainers.
 *
 * Unlike unit tests with mocks, this suite interacts with a real MySQL instance.
 * This approach validates database constraints, JPA mappings, and transaction behavior.
 */
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // --- Helper Methods ---

    private User createAndSaveUser(String name, String email) {
        return userService.saveUser(new User(name, email));
    }

    // --- Test Cases ---

    @Test
    @Order(1)
    @DisplayName("Verify user persistence and auto-generated ID in MySQL")
    void testSaveUser_PersistsToDatabase() {
        User saved = createAndSaveUser("Alice", "alice@example.com");

        assertNotNull(saved.getId(), "The database should assign an ID to the saved user");
        assertEquals("Alice", saved.getName());
        assertEquals("alice@example.com", saved.getEmail());

        assertTrue(userRepository.existsById(saved.getId()));
    }

    @Test
    @Order(2)
    @DisplayName("Ensure user retrieval by ID returns the correct record")
    void testGetUserById_ReturnsCorrectUser() {
        User saved = createAndSaveUser("Bob", "bob@example.com");

        User found = userService.getUserById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals("Bob", found.getName());
        assertEquals("bob@example.com", found.getEmail());
    }

    @Test
    @Order(3)
    @DisplayName("Check that requesting a non-existent ID throws UserNotFoundException")
    void testGetUserById_NotFound_ThrowsException() {
        assertThrows(
            UserService.UserNotFoundException.class,
            () -> userService.getUserById(999_999L)
        );
    }

    @Test
    @Order(4)
    @DisplayName("Validate that non-positive IDs result in an IllegalArgumentException")
    void testGetUserById_InvalidId_ThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(0L));
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(-1L));
    }

    @Test
    @Order(5)
    @DisplayName("Confirm that deleting a user removes them from the database")
    void testDeleteUser_RemovesFromDatabase() {
        User saved = createAndSaveUser("Carol", "carol@example.com");
        long id = saved.getId();

        userService.deleteUser(id);

        assertFalse(userRepository.existsById(id), "User must be removed from the database");
    }

    @Test
    @Order(6)
    @DisplayName("Verify deletion of a non-existent user throws UserNotFoundException")
    void testDeleteUser_NotFound_ThrowsException() {
        assertThrows(
            UserService.UserNotFoundException.class,
            () -> userService.deleteUser(999_999L)
        );
    }

    @Test
    @Order(7)
    @DisplayName("Validate that saving a user with a blank name throws an exception")
    void testSaveUser_BlankName_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> userService.saveUser(new User("", "x@x.com")));
    }
}