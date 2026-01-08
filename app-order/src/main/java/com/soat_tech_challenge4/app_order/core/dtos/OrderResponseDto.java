package com.soat_tech_challenge4.app_order.core.dtos;

import com.soat_tech_challenge4.app_order.core.entities.OrderStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(Long id,
                               LocalDateTime orderDate,
                               OrderStatusEnum orderStatus,
                               List<OrderItemResponseDto> listOrderItemDto,
                               BigDecimal total,
                               Long paymentId) {
}