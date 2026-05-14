package com.tp3.taskmanager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for Task entities.
 * 
 * Provides standard CRUD operations and a custom query method 
 * to filter tasks by their completion status.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    /**
     * Retrieves a list of tasks based on whether they are completed.
     * 
     * @param completed the completion status to filter by
     * @return a list of tasks matching the criteria
     */
    List<Task> findByCompleted(boolean completed);
}