package com.soat_tech_challenge4.app_order.api.data.jpa;

import com.soat_tech_challenge4.app_order.core.entities.Order;
import com.soat_tech_challenge4.app_order.core.entities.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;
    private BigDecimal total;
    private Long paymentId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items;


    public static OrderEntity fromEntity(Order order) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(order.getId());
        orderEntity.setOrderDate(order.getOrderDate());
        orderEntity.setOrderStatus(order.getOrderStatus());
        orderEntity.setTotal(order.getTotal());
        orderEntity.setPaymentId(order.getPaymentId());

        List<OrderItemEntity> itemEntities = order.getItems().stream().map(orderItem -> {
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setId(orderItem.getId());
            orderItemEntity.setProductId(orderItem.getProductId());
            orderItemEntity.setQuantity(orderItem.getQuantity());
            orderItemEntity.setOrder(orderEntity);

            return orderItemEntity;
        }).toList();

        orderEntity.setItems(itemEntities);

        return orderEntity;
    }

}