package com.tp3.exo1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing User entities using Spring Data JPA.
 * 
 * Unlike the mocked version in TP3-part1, Spring provides a concrete 
 * implementation at runtime, facilitating communication with 
 * databases like MySQL through Testcontainers.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // The findById(Long) method is provided by JpaRepository.
    // Standard CRUD operations are automatically implemented.
}