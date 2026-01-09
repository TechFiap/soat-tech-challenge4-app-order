package com.soat_tech_challenge4.app_order.core.controllers;

import com.soat_tech_challenge4.app_order.api.rest.dto.request.OrderRequestDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderItemDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderResponseDto;
import com.soat_tech_challenge4.app_order.core.dtos.ProductDto;
import com.soat_tech_challenge4.app_order.core.entities.Category;
import com.soat_tech_challenge4.app_order.core.entities.Order;
import com.soat_tech_challenge4.app_order.core.entities.OrderItem;
import com.soat_tech_challenge4.app_order.core.entities.OrderStatusEnum;
import com.soat_tech_challenge4.app_order.core.interfaces.DataSource;
import com.soat_tech_challenge4.app_order.core.usecases.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
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
                List.of(new Order(List.of(new OrderItem("d0e950f4-8249-4ea6-95eb-7637e98000c9", 2, BigDecimal.TEN))));

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
        OrderItemDto itemDto = new OrderItemDto(1L, "d0e950f4-8249-4ea6-95eb-7637e98000c9", 5, BigDecimal.TEN);
        OrderRequestDto request = new OrderRequestDto(List.of(itemDto));

        Order order =
                new Order(List.of(new OrderItem("d0e950f4-8249-4ea6-95eb-7637e98000c9", 5, BigDecimal.TEN)));

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
                new Order(List.of(new OrderItem("d0e950f4-8249-4ea6-95eb-7637e98000c9", 1, BigDecimal.TEN)));

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
                List.of(new Order(List.of(new OrderItem("d0e950f4-8249-4ea6-95eb-7637e98000c9", 2, BigDecimal.TEN))));

        try (MockedConstruction<GetAllOrdersSortedUseCase> mocked =
                     mockConstruction(GetAllOrdersSortedUseCase.class,
                             (mock, context) -> when(mock.execute()).thenReturn(orders))) {

            List<OrderDto> result = controller.getAllOrdersSorted();

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Test
    void checkout_success() {
        OrderRequestDto orderRequestDtoMock = new OrderRequestDto(createMockOrderItemDto());
        OrderDto orderDtomock = createMockOrderDto();
        ProductDto productDtoMock = new ProductDto("d0e950f4-8249-4ea6-95eb-7637e98000c9", "Batata Frita",
                "Porçao de batata com queijo", new BigDecimal(25.00),
                Category.ACOMPANHAMENTO, Boolean.TRUE);

        when(dataSource.saveOrder(any())).thenReturn(orderDtomock);
        when(dataSource.findById(any())).thenReturn(productDtoMock);

        OrderResponseDto result = controller.checkout(orderRequestDtoMock);

        assertNotNull(result);
        assertEquals(100L, orderDtomock.id());
        assertEquals(1, orderDtomock.listOrderItemDto().size());

        verify(dataSource).saveOrder(any());
        verify(dataSource, times(2)).findById(any());

    }

    @Test
    void updateStatus_success() {
        OrderDto orderDtomock = createMockOrderDto();
        OrderDto orderDtomockUpdated = createMockOrderDto();
        orderDtomockUpdated.orderStatus().setStatus(OrderStatusEnum.RECEBIDO);

        when(dataSource.findOrderById(any())).thenReturn(orderDtomock);
        when(dataSource.saveOrder(any())).thenReturn(orderDtomockUpdated);

        OrderDto result = controller.updateStatus(100L, "RECEBIDO");

        assertNotNull(result);
        assertEquals("RECEBIDO", result.orderStatus().name());

        verify(dataSource).saveOrder(any());
        verify(dataSource).findOrderById(any());
    }

    @Test
    void getAllOrdersSorted_success() {
        when(dataSource.getAllOrders()).thenReturn(List.of(createMockOrderDto()));

        List<OrderDto> result = controller.getAllOrdersSorted();

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(dataSource).getAllOrders();
    }

    public static OrderDto createMockOrderDto() {
        OrderItemDto item1 = new OrderItemDto(1l, "2ad74ecd-ba58-4d60-999f-010e13e32014",1, new BigDecimal("25.00"));

        OrderDto orderDtomock = new OrderDto(
                100L,
                LocalDateTime.now(),
                OrderStatusEnum.CREATED,
                Arrays.asList(item1),
                new BigDecimal("58.00"),
                999L
        );
        return orderDtomock;
    }

    public static List<OrderItemDto> createMockOrderItemDto() {
        OrderItemDto item1 = new OrderItemDto(1l, "2ad74ecd-ba58-4d60-999f-010e13e32014",1, new BigDecimal("25.00"));
        OrderItemDto item2 = new OrderItemDto(2l, "d0e950f4-8249-4ea6-95eb-7637e98000c9",1, new BigDecimal("8.00"));

        return  Arrays.asList(item1, item2);
    }
}