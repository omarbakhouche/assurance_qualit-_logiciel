package com.tp3.exo2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Order entities.
 *
 * Replaces the previous OrderDao. Spring Data JPA provides the 
 * underlying implementation for standard database operations.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}