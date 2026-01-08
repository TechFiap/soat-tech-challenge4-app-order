package com.soat_tech_challenge4.app_order.api.data.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "orderItems")
@Getter
@Setter
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;
}
