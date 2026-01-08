package com.soat_tech_challenge4.app_order.core.controllers;

import com.soat_tech_challenge4.app_order.api.rest.dto.request.OrderRequestDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderResponseDto;
import com.soat_tech_challenge4.app_order.core.entities.Order;
import com.soat_tech_challenge4.app_order.core.gateways.OrderGateway;
import com.soat_tech_challenge4.app_order.core.gateways.ProductGateway;
import com.soat_tech_challenge4.app_order.core.interfaces.DataSource;
import com.soat_tech_challenge4.app_order.core.presenters.OrderPresenter;
import com.soat_tech_challenge4.app_order.core.usecases.*;

import java.util.List;

public class OrderController {

    private final DataSource dataSource;

    public OrderController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<OrderDto> getAllOrders() {
        OrderGateway orderGateway = new OrderGateway(dataSource);
        GetOrdersUseCase getOrdersUseCase = new GetOrdersUseCase(orderGateway);

        List<Order> listOrders = getOrdersUseCase.execute();

        return OrderPresenter.toDto(listOrders);
    }

    public OrderResponseDto checkout(OrderRequestDto orderRequestDto) {
        OrderGateway orderGateway = new OrderGateway(dataSource);
        ProductGateway productGateway = new ProductGateway(dataSource);
        CheckoutUseCase checkoutUseCase = new CheckoutUseCase(orderGateway, productGateway);

        Order result = checkoutUseCase.execute(orderRequestDto);

        return OrderPresenter.toDtoWithoutOrderItemId(result);
    }

    public OrderDto updateStatus(Long orderId, String orderStatus) {
        OrderGateway orderGateway = new OrderGateway(dataSource);
        UpdateOrderStatusUseCase updateOrderStatusUseCase = new UpdateOrderStatusUseCase(orderGateway);

        Order result = updateOrderStatusUseCase.execute(orderId, orderStatus);

        return OrderPresenter.toDtoWithoutOrderItems(result);
    }

    public OrderDto findById(Long orderId) {
        OrderGateway orderGateway = new OrderGateway(dataSource);
        FindOrderByIdUseCase findOrderByIdUseCase = new FindOrderByIdUseCase(orderGateway);

        Order order = findOrderByIdUseCase.execute(orderId);

        return OrderPresenter.toDto(order);
    }

    public List<OrderDto> getAllOrdersSorted() {
        OrderGateway orderGateway = new OrderGateway(dataSource);
        GetAllOrdersSortedUseCase getAllOrdersSortedUseCase = new GetAllOrdersSortedUseCase(orderGateway);

        List<Order> listOrders = getAllOrdersSortedUseCase.execute();

        return OrderPresenter.toDtoWithOrderItemDtoList(listOrders);
    }
}
