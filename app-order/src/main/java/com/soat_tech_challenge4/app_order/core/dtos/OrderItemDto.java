package com.soat_tech_challenge4.app_order.core.dtos;

import java.math.BigDecimal;

public record OrderItemDto(
        Long id,
        Long productId,
        Integer quantity,
        BigDecimal price) {
}
