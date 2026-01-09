package com.soat_tech_challenge4.app_order.core.presenters;

import com.soat_tech_challenge4.app_order.core.dtos.OrderDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderItemDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderItemResponseDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderResponseDto;
import com.soat_tech_challenge4.app_order.core.entities.Order;

import java.util.List;

public class OrderPresenter {

    public static List<OrderDto> toDto(List<Order> listOrder) {
        return listOrder.stream()
                .map((order -> new OrderDto(
                        order.getId(),
                        order.getOrderDate(),
                        order.getOrderStatus(),
                        List.of(),
                        order.getTotal(),
                        order.getPaymentId())))
                .toList();
    }

    public static OrderDto toDto(Order order) {
        List<OrderItemDto> orderItemDtoList = order.getItems().stream()
                .map(orderItem ->
                     new OrderItemDto(
                            orderItem.getId(),
                            orderItem.getProductId(),
                            orderItem.getQuantity(),
                            orderItem.getPrice()
                     ))
                .toList();
        return new OrderDto(
                order.getId(),
                order.getOrderDate(),
                order.getOrderStatus(),
                orderItemDtoList,
                order.getTotal(),
                order.getPaymentId());
    }

    public static OrderDto toDtoWithoutOrderItems(Order order) {
        return new OrderDto(
                order.getId(),
                order.getOrderDate(),
                order.getOrderStatus(),
                List.of(),
                order.getTotal(),
                order.getPaymentId());
    }

    public static List<OrderDto> toDtoWithOrderItemDtoList(List<Order> listOrder) {
        return listOrder.stream()
                .map(order -> {
                    List<OrderItemDto> items = order.getItems().stream()
                            .map(orderItem -> new OrderItemDto(
                                    orderItem.getId(),
                                    orderItem.getProductId(),
                                    orderItem.getQuantity(),
                                    orderItem.getPrice()
                            ))
                            .toList();

                    return new OrderDto(
                            order.getId(),
                            order.getOrderDate(),
                            order.getOrderStatus(),
                            items,
                            order.getTotal(),
                            order.getPaymentId()
                    );
                })
                .toList();
    }

    public static OrderResponseDto toDtoWithoutOrderItemId(Order order) {
        List<OrderItemResponseDto> orderItemDtoList = order.getItems().stream()
                .map(orderItem -> {
                    return new OrderItemResponseDto(
                            orderItem.getProductId(),
                            orderItem.getQuantity(),
                            orderItem.getPrice()
                    );
                })
                .toList();
        return new OrderResponseDto(
                order.getId(),
                order.getOrderDate(),
                order.getOrderStatus(),
                orderItemDtoList,
                order.getTotal(),
                order.getPaymentId());
    }
}
