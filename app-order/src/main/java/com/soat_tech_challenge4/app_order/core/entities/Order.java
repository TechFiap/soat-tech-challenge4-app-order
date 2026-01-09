package com.soat_tech_challenge4.app_order.core.entities;

import com.soat_tech_challenge4.app_order.application.exceptions.ErrorException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public class Order {
    private Long id;
    private final LocalDateTime orderDate;
    private OrderStatusEnum orderStatus;
    private final List<OrderItem> items;
    private final BigDecimal total;
    private Long paymentId;

    public Order(List<OrderItem> items) {
        this.items = items;
        this.orderDate = LocalDateTime.now();
        this.orderStatus = OrderStatusEnum.CREATED;
        this.total = calculateTotal(); // soma dos itens * quantidade
    }

    // setter opcional para setar o id vindo do banco
    public void setId(Long id) {
        this.id = id;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimal calculateTotal() {
        return items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void updateStatus(String status) {
        if(Arrays.stream(OrderStatusEnum.values())
                .noneMatch(value -> value.toString().equals(status))) {
            throw new IllegalArgumentException("Order status not recognized");
        }

        if(status.equalsIgnoreCase(this.orderStatus.name())) {
            throw new ErrorException("Order status is the same");
        }

        this.orderStatus = OrderStatusEnum.valueOf(status);
    }
}