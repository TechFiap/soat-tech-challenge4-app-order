package com.soat_tech_challenge4.app_order.core.gateways;

import com.soat_tech_challenge4.app_order.core.dtos.OrderDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderItemDto;
import com.soat_tech_challenge4.app_order.core.entities.Order;
import com.soat_tech_challenge4.app_order.core.entities.OrderItem;
import com.soat_tech_challenge4.app_order.core.interfaces.DataSource;
import com.soat_tech_challenge4.app_order.core.interfaces.IOrderGateway;

import java.util.List;

public class OrderGateway implements IOrderGateway {

    private final DataSource dataSource;

    public OrderGateway(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Order> getAllOrders() {
        List<OrderDto> result = dataSource.getAllOrders();

        List<Order> listOrders = result.stream()
                .map(orderDto -> {
                    List<OrderItem> items = orderDto.listOrderItemDto().stream()
                            .map(orderItemDto -> new OrderItem(
                                    orderItemDto.id(),
                                    orderItemDto.productId(),
                                    orderItemDto.quantity(),
                                    orderItemDto.price()
                            ))
                            .toList();

                    return new Order(
                            orderDto.id(),
                            orderDto.orderDate(),
                            orderDto.orderStatus(),
                            items,
                            orderDto.total(),
                            orderDto.paymentId()
                    );
                })
                .toList();

        return listOrders;
    }

    @Override
    public Order save(Order order) {
        List<OrderItemDto> orderItemDtoList = order.getItems().stream().map(item -> new OrderItemDto(
                        item.getId(),
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice()))
                .toList();

        OrderDto orderDto = new OrderDto(
                order.getId(),
                order.getOrderDate(),
                order.getOrderStatus(),
                orderItemDtoList,
                order.getTotal(),
                order.getPaymentId());

        OrderDto saveOrder = dataSource.saveOrder(orderDto);
        order.setId(saveOrder.id());

        return order;
    }

    @Override
    public Order findById(Long orderId) {
        OrderDto order = dataSource.findOrderById(orderId);

        if (order == null) return null;

        return mapToOrder(order);
    }

    private Order mapToOrder(OrderDto orderDto) {
        List<OrderItem> orderItems = orderDto.listOrderItemDto().stream()
                .map(itemDto -> new OrderItem(
                        itemDto.id(),
                        itemDto.productId(),
                        itemDto.quantity(),
                        itemDto.price()))
                .toList();

        Order order = new Order(
                orderDto.id(),
                orderDto.orderDate(),
                orderDto.orderStatus(),
                orderItems,
                orderDto.total(),
                orderDto.paymentId());

        return order;
    }
}