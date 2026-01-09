package com.soat_tech_challenge4.app_order.core.dtos;

import com.soat_tech_challenge4.app_order.core.entities.Category;

import java.math.BigDecimal;

public record ProductDto(String id,
                         String name,
                         String description,
                         BigDecimal price,
                         Category category,
                         boolean avaliable) {
}