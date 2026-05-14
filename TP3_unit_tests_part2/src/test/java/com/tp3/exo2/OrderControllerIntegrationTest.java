package com.tp3.exo2;

import com.tp3.AbstractIntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for the Exo2 sequence: Controller -> Service -> Repository.
 * 
 * Unlike the unit tests in Part 1 that utilized Mockito, this suite verifies 
 * the entire chain using a live MySQL instance via Testcontainers.
 * 
 * Validation includes:
 * - End-to-end persistence confirmation.
 * - Database state verification after deletion.
 * - Boundary validation logic.
 */
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private OrderController orderController;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    // --- Helper ---

    private Order makeOrder(String product, int qty, double price) {
        return new Order(product, qty, price);
    }

    // --- Test Methods ---

    @Test
    @Order(1)
    @DisplayName("Should persist order to MySQL when created through the service")
    void testCreateOrder_PersistedToDatabase() {
        Order input = makeOrder("Laptop", 2, 999.99);
        Order saved = orderService.createOrder(input);

        assertNotNull(saved.getId(), "Generated ID should be present");
        assertEquals("Laptop", saved.getProductName());
        assertEquals(2, saved.getQuantity());
        assertEquals(999.99, saved.getPrice(), 1e-9);

        assertTrue(orderRepository.existsById(saved.getId()), "Order must exist in the database");
    }

    @Test
    @Order(2)
    @DisplayName("Should retrieve a previously saved order by its ID")
    void testFindOrderById_ReturnsCorrectOrder() {
        Order saved = orderService.createOrder(makeOrder("Mouse", 5, 29.99));

        Optional<Order> found = orderService.findOrderById(saved.getId());

        assertTrue(found.isPresent(), "Order should be found");
        assertEquals("Mouse", found.get().getProductName());
        assertEquals(5, found.get().getQuantity());
    }

    @Test
    @Order(3)
    @DisplayName("Should return empty Optional when searching for a non-existent ID")
    void testFindOrderById_NotFound_ReturnsEmpty() {
        Optional<Order> result = orderService.findOrderById(999_999L);
        assertTrue(result.isEmpty(), "Result should be empty for unknown IDs");
    }

    @Test
    @Order(4)
    @DisplayName("Should remove the specified order from the database")
    void testDeleteOrderById_RemovesFromDatabase() {
        Order saved = orderService.createOrder(makeOrder("Keyboard", 1, 79.99));
        long id = saved.getId();

        orderService.deleteOrderById(id);

        assertFalse(orderRepository.existsById(id), "Order should be deleted from MySQL");
    }

    @Test
    @Order(5)
    @DisplayName("Should throw exception if the order object is null")
    void testCreateOrder_NullOrder_Throws() {
        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(null));
    }

    @Test
    @Order(6)
    @DisplayName("Should throw exception for orders with blank product names")
    void testCreateOrder_BlankProductName_Throws() {
        assertThrows(IllegalArgumentException.class,
            () -> orderService.createOrder(new Order("", 1, 10.0)));
    }

    @Test
    @Order(7)
    @DisplayName("Should throw exception for non-positive quantities")
    void testCreateOrder_InvalidQuantity_Throws() {
        assertThrows(IllegalArgumentException.class,
            () -> orderService.createOrder(new Order("Widget", 0, 5.0)));
        assertThrows(IllegalArgumentException.class,
            () -> orderService.createOrder(new Order("Widget", -3, 5.0)));
    }

    @Test
    @Order(8)
    @DisplayName("Should ensure multiple orders are persisted with unique IDs")
    void testCreateOrder_MultipleOrders_AllPersisted() {
        Order o1 = orderService.createOrder(makeOrder("Monitor", 1, 350.0));
        Order o2 = orderService.createOrder(makeOrder("Chair", 2, 200.0));

        assertNotEquals(o1.getId(), o2.getId(), "IDs must be unique");
        assertTrue(orderRepository.existsById(o1.getId()));
        assertTrue(orderRepository.existsById(o2.getId()));
    }
}