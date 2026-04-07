package com.tp3.exercise2;

public class OrderService {

    private final OrderDao orderDao;

    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void createOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("La commande ne peut pas être nulle");
        }
        orderDao.saveOrder(order);
    }
}
