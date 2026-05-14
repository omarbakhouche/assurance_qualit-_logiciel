package com.tp3.exo2;

import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order) {
        if (order == null)
            throw new IllegalArgumentException("Order must not be null");
        if (order.getProductName() == null || order.getProductName().isBlank())
            throw new IllegalArgumentException("Product name must not be blank");
        if (order.getQuantity() <= 0)
            throw new IllegalArgumentException("Quantity must be positive");
        return orderRepository.save(order);
    }

    public Optional<Order> findOrderById(long id) {
        return orderRepository.findById(id);
    }

    public void deleteOrderById(long id) {
        if (!orderRepository.existsById(id))
            throw new IllegalArgumentException("No order found with id: " + id);
        orderRepository.deleteById(id);
    }
}
