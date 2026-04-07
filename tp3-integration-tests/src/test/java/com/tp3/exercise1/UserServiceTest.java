package com.tp3.exercise1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Exercice 1 - UserService Integration Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("getUserById doit retourner l'utilisateur correspondant à l'id donné")
    void testGetUserById_ReturnsCorrectUser() {
        // Arrange
        long userId = 1L;
        User expectedUser = new User(userId, "Alice Dupont", "alice@example.com");
        when(userRepository.findUserById(userId)).thenReturn(expectedUser);

        // Act
        User actualUser = userService.getUserById(userId);

        // Assert
        assertNotNull(actualUser);
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());

        // Vérifier que findUserById a été appelé avec le bon argument
        verify(userRepository, times(1)).findUserById(userId);
    }

    @Test
    @DisplayName("getUserById doit retourner null si l'utilisateur n'existe pas")
    void testGetUserById_ReturnsNull_WhenUserNotFound() {
        // Arrange
        long userId = 99L;
        when(userRepository.findUserById(userId)).thenReturn(null);

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findUserById(userId);
    }

    @Test
    @DisplayName("getUserById doit appeler findUserById avec le bon argument")
    void testGetUserById_CallsRepositoryWithCorrectId() {
        // Arrange
        long userId = 42L;
        when(userRepository.findUserById(anyLong())).thenReturn(null);

        // Act
        userService.getUserById(userId);

        // Assert : findUserById doit être appelé avec exactement userId=42
        verify(userRepository).findUserById(42L);
        verify(userRepository, never()).findUserById(1L);
    }
}
