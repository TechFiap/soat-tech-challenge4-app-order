package com.soat_tech_challenge4.app_order.core.usecases;

import com.soat_tech_challenge4.app_order.core.entities.Order;
import com.soat_tech_challenge4.app_order.core.gateways.OrderGateway;

import java.util.List;

public class GetOrdersUseCase {

    private final OrderGateway orderGateway;

    public GetOrdersUseCase(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    public List<Order> execute() {
        List<Order> result = orderGateway.getAllOrders();

        if (result == null) return null;

        return result;
    };
}