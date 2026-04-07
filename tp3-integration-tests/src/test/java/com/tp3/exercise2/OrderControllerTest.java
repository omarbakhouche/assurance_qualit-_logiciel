package com.tp3.exercise2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Exercice 2 - OrderController Integration Tests")
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private OrderDao orderDao;

    private OrderController orderController;

    @BeforeEach
    void setUp() {
        orderController = new OrderController(orderService);
    }

    @Test
    @DisplayName("createOrder doit appeler OrderService.createOrder avec la bonne commande")
    void testCreateOrder_CallsServiceWithCorrectOrder() {
        // Arrange
        Order order = new Order(1L, "Laptop", 2, 1500.00);
        doNothing().when(orderService).createOrder(order);

        // Act
        orderController.createOrder(order);

        // Assert : vérifier que orderService.createOrder est appelé avec le bon argument
        verify(orderService, times(1)).createOrder(order);
    }

    @Test
    @DisplayName("createOrder doit propager l'appel jusqu'à OrderDao via OrderService")
    void testCreateOrder_PropagatesCallToDao() {
        // Arrange
        Order order = new Order(2L, "Phone", 1, 800.00);

        // Créer un vrai OrderService branché sur le mock OrderDao
        OrderService realOrderService = new OrderService(orderDao);
        OrderController controllerWithRealService = new OrderController(realOrderService);

        // Act
        controllerWithRealService.createOrder(order);

        // Assert : vérifier que orderDao.saveOrder est appelé avec l'objet commande
        verify(orderDao, times(1)).saveOrder(order);
    }

    @Test
    @DisplayName("createOrder doit propager l'exception si OrderService lève une erreur")
    void testCreateOrder_ThrowsException_WhenServiceFails() {
        // Arrange
        Order order = new Order(3L, "Tablet", 3, 600.00);
        doThrow(new RuntimeException("Erreur base de données"))
                .when(orderService).createOrder(order);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderController.createOrder(order));
        assertEquals("Erreur base de données", exception.getMessage());
        verify(orderService, times(1)).createOrder(order);
    }

    @Test
    @DisplayName("createOrder ne doit pas appeler le service si la commande est nulle (validation dans le service)")
    void testCreateOrder_NullOrder_ThrowsException() {
        // Arrange
        OrderService realOrderService = new OrderService(orderDao);
        OrderController controllerWithRealService = new OrderController(realOrderService);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controllerWithRealService.createOrder(null));

        // saveOrder ne doit jamais être appelé si la commande est nulle
        verify(orderDao, never()).saveOrder(any());
    }
}
