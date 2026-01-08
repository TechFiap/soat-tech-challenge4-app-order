package com.soat_tech_challenge4.app_order.api.rest.dto.request;

import com.soat_tech_challenge4.app_order.core.dtos.OrderItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class OrderRequestDto {
    private List<OrderItemDto> items;
}
