package com.soat_tech_challenge4.app_order.core.controllers;

import com.soat_tech_challenge4.app_order.api.rest.dto.request.OrderRequestDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderItemDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderResponseDto;
import com.soat_tech_challenge4.app_order.core.dtos.ProductDto;
import com.soat_tech_challenge4.app_order.core.entities.Category;
import com.soat_tech_challenge4.app_order.core.entities.OrderStatusEnum;
import com.soat_tech_challenge4.app_order.core.gateways.OrderGateway;
import com.soat_tech_challenge4.app_order.core.interfaces.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class OrderControllerTest {

    private OrderController orderController;
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        dataSource = mock(DataSource.class);
        OrderGateway orderGateway = mock(OrderGateway.class);
        orderController = new OrderController(dataSource);
    }

    @Test
    void updateStatus_success() {
        OrderDto orderDtomock = createMockOrderDto();
        OrderDto orderDtomockUpdated = createMockOrderDto();
        orderDtomockUpdated.orderStatus().setStatus(OrderStatusEnum.RECEBIDO);

        when(dataSource.findOrderById(any())).thenReturn(orderDtomock);
        when(dataSource.saveOrder(any())).thenReturn(orderDtomockUpdated);

        OrderDto result = orderController.updateStatus(100L, "RECEBIDO");

        assertNotNull(result);
        assertEquals("RECEBIDO", result.orderStatus().name());

        verify(dataSource).saveOrder(any());
        verify(dataSource).findOrderById(any());
    }

    @Test
    void getAllOrdersSorted_success() {
        when(dataSource.getAllOrders()).thenReturn(List.of(createMockOrderDto()));

        List<OrderDto> result = orderController.getAllOrdersSorted();

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(dataSource).getAllOrders();
    }

    public static OrderDto createMockOrderDto() {
        OrderItemDto item1 = new OrderItemDto(1l, 10l,1, new BigDecimal("25.00"));

        return new OrderDto(
                100L,
                LocalDateTime.now(),
                OrderStatusEnum.CREATED,
                List.of(item1),
                new BigDecimal("58.00"),
                999L
        );
    }

    public static List<OrderItemDto> createMockOrderItemDto() {
        OrderItemDto item1 = new OrderItemDto(1l, 10l,1, new BigDecimal("25.00"));
        OrderItemDto item2 = new OrderItemDto(2l, 20l,1, new BigDecimal("8.00"));

        return  Arrays.asList(item1, item2);
    }
}