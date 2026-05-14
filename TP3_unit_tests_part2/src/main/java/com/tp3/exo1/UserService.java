package com.tp3.exo1;

import org.springframework.stereotype.Service;

/**
 * Service layer handling User business logic.
 *
 * Maintains the same logic as TP3-part1, now managed by Spring as a @Service.
 * Interacts with a real JPA repository for data persistence.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("User id must be positive, got: " + id);
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No user found with id: " + id));
    }

    public User saveUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            throw new IllegalArgumentException("User name must not be blank");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("User email must not be blank");
        }
        return userRepository.save(user);
    }

    public void deleteUser(long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("No user found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}