package com.soat_tech_challenge4.app_order.api.rest.dto.response;

import com.soat_tech_challenge4.app_order.core.entities.Category;

import java.math.BigDecimal;

public record ExternalProductResponse(String id,
                                      String name,
                                      String description,
                                      BigDecimal price,
                                      Category category,
                                      boolean avaliable) {}
