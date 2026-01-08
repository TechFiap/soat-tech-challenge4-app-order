package com.soat_tech_challenge4.app_order.core.usecases;

import com.soat_tech_challenge4.app_order.application.exceptions.ErrorException;
import com.soat_tech_challenge4.app_order.core.entities.Order;
import com.soat_tech_challenge4.app_order.core.gateways.OrderGateway;

public class UpdateOrderStatusUseCase {

    private final OrderGateway orderGateway;

    public UpdateOrderStatusUseCase(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    public Order execute(Long orderId, String orderStatus) {

        Order order = orderGateway.findById(orderId);

        if (order == null) {
            throw new ErrorException("Order not found with id " + orderId);
        }

        order.updateStatus(orderStatus);

        Order orderUpdated = orderGateway.save(order);

        return orderUpdated;
    }
}
