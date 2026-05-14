package com.tp3;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Base class for all integration tests using Testcontainers.
 *
 * This abstract class facilitates the shared container pattern, ensuring a single 
 * MySQL instance is utilized across the entire test suite to optimize performance.
 *
 * Key components:
 * - @Container: Initializes the MySQL container once per JVM.
 * - @DynamicPropertySource: Injects the container's dynamic connection details 
 *   into the Spring environment.
 */
@Testcontainers
public abstract class AbstractIntegrationTest {

    @Container
    static final MySQLContainer<?> MYSQL =
            new MySQLContainer<>("mysql:8.0")
                    .withDatabaseName("tp3_test_db")
                    .withUsername("tp3user")
                    .withPassword("tp3pass")
                    .withReuse(true);

    /**
     * Dynamically configures the Spring datasource to point to the 
     * Testcontainers MySQL instance.
     */
    @DynamicPropertySource
    static void overrideDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }
}