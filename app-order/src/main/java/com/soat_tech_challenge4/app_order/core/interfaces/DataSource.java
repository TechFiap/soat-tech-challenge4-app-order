package com.soat_tech_challenge4.app_order.core.interfaces;

import com.soat_tech_challenge4.app_order.core.dtos.OrderDto;
import com.soat_tech_challenge4.app_order.core.dtos.ProductDto;

import java.util.List;

public interface DataSource {

    OrderDto findOrderById(Long orderId);

    List<OrderDto> getAllOrders();

    OrderDto saveOrder(OrderDto orderDto);

    ProductDto findById(Long id);
}
