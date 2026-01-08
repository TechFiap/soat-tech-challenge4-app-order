package com.soat_tech_challenge4.app_order.core.interfaces;

import com.soat_tech_challenge4.app_order.core.entities.Order;

import java.util.List;

public interface IOrderGateway {
    List<Order> getAllOrders();
    Order save(Order order);
    Order findById(Long orderId);
}