package com.soat_tech_challenge4.app_order.core.usecases;

import com.soat_tech_challenge4.app_order.api.rest.dto.request.OrderRequestDto;
import com.soat_tech_challenge4.app_order.core.entities.Order;
import com.soat_tech_challenge4.app_order.core.gateways.OrderGateway;
import com.soat_tech_challenge4.app_order.core.gateways.ProductGateway;

public class CheckoutUseCase {

    private final OrderGateway orderGateway;
    private final ProductGateway productGateway;

    public CheckoutUseCase(OrderGateway orderGateway, ProductGateway productGateway) {
        this.orderGateway = orderGateway;
        this.productGateway = productGateway;
    }

    public Order execute(OrderRequestDto orderRequestDto) {
        CreateOrderUseCase createOrderUseCase = new CreateOrderUseCase(orderGateway, productGateway);

        //Chama o useCase para criar a order
        Order order = createOrderUseCase.execute(orderRequestDto);

        return order;
    }
}
