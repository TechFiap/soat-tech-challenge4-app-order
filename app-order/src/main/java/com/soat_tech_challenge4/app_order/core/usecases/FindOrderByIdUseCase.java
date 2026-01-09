package com.soat_tech_challenge4.app_order.core.usecases;

import com.soat_tech_challenge4.app_order.core.entities.Order;
import com.soat_tech_challenge4.app_order.core.gateways.OrderGateway;

public class FindOrderByIdUseCase {

    private final OrderGateway orderGateway;

    public FindOrderByIdUseCase(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    public Order execute(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        return orderGateway.findById(orderId);
    }
}
