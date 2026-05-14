package com.tp3.exo3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Product entities.
 * 
 * Leverages Spring Data JPA to provide standard persistence operations 
 * for products stored in the database.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
}