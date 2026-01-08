package com.soat_tech_challenge4.app_order.core.dtos;

import com.soat_tech_challenge4.app_order.core.entities.OrderStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        Long id,
        LocalDateTime orderDate,
        OrderStatusEnum orderStatus,
        List<OrderItemDto> listOrderItemDto,
        BigDecimal total,
        Long paymentId
) {}