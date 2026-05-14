package com.tp3.exo3;

import com.tp3.AbstractIntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test suite for ProductService utilizing Testcontainers.
 *
 * This version replaces the mocked API from Part 1 with a live MySQL instance.
 * Scenarios verify persistence, data integrity, and error handling 
 * against a real database environment.
 */
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    // --- Helper Methods ---

    private Product saveProduct(String id, String name, double price, String category) {
        return productService.saveProduct(new Product(id, name, price, category));
    }

    // --- Scenario 1: Retrieval Success ---

    @Test
    @Order(1)
    @DisplayName("Verify that getProduct correctly retrieves data from MySQL")
    void scenario1_GetProduct_Success() {
        saveProduct("PROD-1", "Wireless Mouse", 29.99, "Electronics");

        Product result = productService.getProduct("PROD-1");

        assertNotNull(result);
        assertEquals("PROD-1", result.getId());
        assertEquals("Wireless Mouse", result.getName());
        assertEquals(29.99, result.getPrice(), 1e-9);
        assertEquals("Electronics", result.getCategory());
    }

    @Test
    @Order(2)
    @DisplayName("Confirm round-trip: saved product is immediately retrievable")
    void scenario1_RoundTrip() {
        Product saved = saveProduct("PROD-2", "Keyboard", 79.99, "Peripherals");
        Product fetched = productService.getProduct(saved.getId());

        assertEquals(saved.getId(), fetched.getId());
        assertEquals(saved.getName(), fetched.getName());
        assertEquals(saved.getPrice(), fetched.getPrice(), 1e-9);
        assertEquals(saved.getCategory(), fetched.getCategory());
    }

    // --- Scenario 2: Invalid Data Formatting ---

    @Test
    @Order(3)
    @DisplayName("Verify saveProduct throws exception for blank product names")
    void scenario2_InvalidData_BlankName_ThrowsException() {
        assertThrows(
            IllegalArgumentException.class,
            () -> saveProduct("PROD-BAD", "", 10.0, "Unknown")
        );
    }

    @Test
    @Order(4)
    @DisplayName("Verify saveProduct throws exception for null product objects")
    void scenario2_InvalidData_NullProduct_ThrowsException() {
        assertThrows(
            IllegalArgumentException.class,
            () -> productService.saveProduct(null)
        );
    }

    @Test
    @Order(5)
    @DisplayName("Verify saveProduct throws exception for null IDs")
    void scenario2_InvalidData_NullId_ThrowsException() {
        assertThrows(
            IllegalArgumentException.class,
            () -> productService.saveProduct(new Product(null, "Name", 1.0, "Cat"))
        );
    }

    // --- Scenario 3: Resource Missing / ID Errors ---

    @Test
    @Order(6)
    @DisplayName("Verify getProduct throws ProductNotFoundException for unknown IDs")
    void scenario3_ApiFailure_ProductNotFound_ThrowsException() {
        ProductService.ProductNotFoundException ex = assertThrows(
            ProductService.ProductNotFoundException.class,
            () -> productService.getProduct("DOES-NOT-EXIST")
        );
        assertTrue(ex.getMessage().contains("DOES-NOT-EXIST"));
    }

    @Test
    @Order(7)
    @DisplayName("Verify getProduct throws exception for null or blank IDs")
    void scenario3_BlankProductId_ThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> productService.getProduct(""));
        assertThrows(IllegalArgumentException.class, () -> productService.getProduct(null));
    }

    // --- New Scenario: Data Deletion ---

    @Test
    @Order(8)
    @DisplayName("Verify deleteProduct removes the record from MySQL")
    void newScenario_DeleteProduct_RemovedFromDatabase() {
        saveProduct("PROD-DEL", "Monitor", 350.0, "Electronics");

        productService.deleteProduct("PROD-DEL");

        assertFalse(productRepository.existsById("PROD-DEL"), "The product record should be deleted");
    }

    @Test
    @Order(9)
    @DisplayName("Verify deleteProduct throws exception for non-existent products")
    void newScenario_DeleteProduct_NotFound_Throws() {
        assertThrows(
            ProductService.ProductNotFoundException.class,
            () -> productService.deleteProduct("GHOST-PROD")
        );
    }
}