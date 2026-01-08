package com.soat_tech_challenge4.app_order.core.usecases;

import com.soat_tech_challenge4.app_order.api.rest.dto.request.OrderRequestDto;
import com.soat_tech_challenge4.app_order.application.exceptions.ErrorException;
import com.soat_tech_challenge4.app_order.core.entities.Order;
import com.soat_tech_challenge4.app_order.core.entities.OrderItem;
import com.soat_tech_challenge4.app_order.core.entities.Product;
import com.soat_tech_challenge4.app_order.core.gateways.OrderGateway;
import com.soat_tech_challenge4.app_order.core.gateways.ProductGateway;

import java.util.List;
import java.util.stream.Collectors;

public class CreateOrderUseCase {

    private final OrderGateway orderGateway;
    private final ProductGateway productGateway;

    public CreateOrderUseCase(OrderGateway orderGateway, ProductGateway productGateway) {
        this.orderGateway = orderGateway;
        this.productGateway = productGateway;
    }

    public Order execute(OrderRequestDto orderRequestDto) {
        List<OrderItem> items = orderRequestDto.getItems().stream()
                .map(item -> {
                    OrderItem orderItem = new OrderItem(item.productId(), item.quantity(), item.price());
                    Product product = productGateway.findById(item.productId());

                    if (product == null) {
                        throw new ErrorException("Product with id " + item.productId() + " not found in database");
                    }

                    return orderItem;
                })
                .collect(Collectors.toList());

        Order newOrder = new Order(items);

        Order savedOrder = orderGateway.save(newOrder);

        return savedOrder;
    }
}
