package com.soat_tech_challenge4.app_order.core.usecases;

import com.soat_tech_challenge4.app_order.core.entities.Order;
import com.soat_tech_challenge4.app_order.core.gateways.OrderGateway;

import java.util.Comparator;
import java.util.List;

public class GetAllOrdersSortedUseCase {

    private static final String FINALIZADO = "FINALIZADO";

    private final OrderGateway orderGateway;

    public GetAllOrdersSortedUseCase(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    public List<Order> execute() {
        List<Order> result = orderGateway.getAllOrders();
        return result.stream()
                .filter(order -> order.getOrderStatus() != null && !order.getOrderStatus().name()
                        .equalsIgnoreCase(FINALIZADO))
                .sorted(Comparator
                        .comparing((Order order) -> getStatusPriority(order.getOrderStatus().name()))
                        .thenComparing(Order::getOrderDate))
                .toList();
    }

    private int getStatusPriority(String status) {
        return switch (status.toUpperCase()) {
            case "PRONTO" -> 1;
            case "EM_PREPARACAO" -> 2;
            case "RECEBIDO" -> 3;
            default -> 99;
        };
    }
}
