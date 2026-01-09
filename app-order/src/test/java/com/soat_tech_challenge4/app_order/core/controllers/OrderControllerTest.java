package com.soat_tech_challenge4.app_order.core.controllers;

import com.soat_tech_challenge4.app_order.api.rest.dto.request.OrderRequestDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderItemDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderResponseDto;
import com.soat_tech_challenge4.app_order.core.entities.Order;
import com.soat_tech_challenge4.app_order.core.entities.OrderItem;
import com.soat_tech_challenge4.app_order.core.interfaces.DataSource;
import com.soat_tech_challenge4.app_order.core.usecases.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    private DataSource dataSource;
    private OrderController controller;

    @BeforeEach
    void setUp() {
        dataSource = mock(DataSource.class);
        controller = new OrderController(dataSource);
    }

    @Test
    void getAllOrders_shouldReturnOrderDtoList() {
        List<Order> orders =
                List.of(new Order(List.of(new OrderItem(1L, 2, BigDecimal.TEN))));

        try (MockedConstruction<GetOrdersUseCase> mocked =
                     mockConstruction(GetOrdersUseCase.class,
                             (mock, context) -> when(mock.execute()).thenReturn(orders))) {

            List<OrderDto> result = controller.getAllOrders();

            assertNotNull(result);
            assertEquals(1, result.size());
            //assertEquals(1, result.get(0).listOrderItemDto().size());
        }
    }

    @Test
    void checkout_shouldReturnOrderResponseDto() {
        OrderItemDto itemDto = new OrderItemDto(1L, 2L, 5, BigDecimal.TEN);
        OrderRequestDto request = new OrderRequestDto(List.of(itemDto));

        Order order =
                new Order(List.of(new OrderItem(2L, 5, BigDecimal.TEN)));

        try (MockedConstruction<CheckoutUseCase> mocked =
                     mockConstruction(CheckoutUseCase.class,
                             (mock, context) ->
                                     when(mock.execute(request)).thenReturn(order))) {

            OrderResponseDto response = controller.checkout(request);

            assertNotNull(response);
            assertEquals(1, response.listOrderItemDto().size());
        }
    }

    @Test
    void findById_shouldReturnOrderDto() {
        Order order =
                new Order(List.of(new OrderItem(1L, 1, BigDecimal.TEN)));

        try (MockedConstruction<FindOrderByIdUseCase> mocked =
                     mockConstruction(FindOrderByIdUseCase.class,
                             (mock, context) ->
                                     when(mock.execute(5L)).thenReturn(order))) {

            OrderDto result = controller.findById(5L);

            assertNotNull(result);
            assertEquals(1, result.listOrderItemDto().size());
        }
    }

    @Test
    void getAllOrdersSorted_shouldReturnSortedOrderDtoList() {
        List<Order> orders =
                List.of(new Order(List.of(new OrderItem(1L, 2, BigDecimal.TEN))));

        try (MockedConstruction<GetAllOrdersSortedUseCase> mocked =
                     mockConstruction(GetAllOrdersSortedUseCase.class,
                             (mock, context) -> when(mock.execute()).thenReturn(orders))) {

            List<OrderDto> result = controller.getAllOrdersSorted();

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }
}
