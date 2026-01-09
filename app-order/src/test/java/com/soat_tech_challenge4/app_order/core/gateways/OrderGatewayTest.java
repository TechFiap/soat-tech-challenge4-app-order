package com.soat_tech_challenge4.app_order.core.gateways;

import com.soat_tech_challenge4.app_order.core.dtos.OrderDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderItemDto;
import com.soat_tech_challenge4.app_order.core.entities.Order;
import com.soat_tech_challenge4.app_order.core.entities.OrderItem;
import com.soat_tech_challenge4.app_order.core.entities.OrderStatusEnum;
import com.soat_tech_challenge4.app_order.core.interfaces.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderGatewayTest {

    private DataSource dataSource;
    private OrderGateway gateway;

    @BeforeEach
    void setUp() {
        dataSource = mock(DataSource.class);
        gateway = new OrderGateway(dataSource);
    }

    @Test
    void getAllOrders_shouldMapOrderDtoToOrderEntity() {
        OrderItemDto itemDto =
                new OrderItemDto(1L, "d0e950f4-8249-4ea6-95eb-7637e98000c9", 2, BigDecimal.valueOf(15.0));

        OrderDto orderDto =
                new OrderDto(
                        5L,
                        LocalDateTime.now(),
                        OrderStatusEnum.valueOf("CREATED"),
                        List.of(itemDto), BigDecimal.valueOf(30.0),
                        1L
                );

        when(dataSource.getAllOrders()).thenReturn(List.of(orderDto));

        List<Order> result = gateway.getAllOrders();

        assertNotNull(result);
        assertEquals(1, result.size());

        Order order = result.get(0);
        assertEquals(5L, order.getId());
        assertEquals(OrderStatusEnum.valueOf("CREATED"), order.getOrderStatus());
        assertEquals(BigDecimal.valueOf(30.0), order.getTotal());
        assertEquals(1L, order.getPaymentId());

        assertEquals(1, order.getItems().size());
        OrderItem item = order.getItems().get(0);
        assertEquals(1L, item.getId());
        assertEquals("d0e950f4-8249-4ea6-95eb-7637e98000c9", item.getProductId());
        assertEquals(2, item.getQuantity());
        assertEquals(BigDecimal.valueOf(15.0), item.getPrice());
    }

    @Test
    void save_shouldMapOrderToDtoCallDataSourceAndUpdateOrderId() {
        // Arrange
        OrderItem item = new OrderItem(
                "d0e950f4-8249-4ea6-95eb-7637e98000c9",
                2,
                BigDecimal.valueOf(25.0)
        );

        Order order = new Order(
                null,
                LocalDateTime.now(),
                OrderStatusEnum.valueOf("CREATED"),
                List.of(item),
                BigDecimal.valueOf(50.0),
                1L
        );

        OrderDto savedOrderDto = new OrderDto(
                99L,
                order.getOrderDate(),
                order.getOrderStatus(),
                List.of(new OrderItemDto(
                        99L,
                        "d0e950f4-8249-4ea6-95eb-7637e98000c9",
                        2,
                        BigDecimal.valueOf(25.0)
                )),
                BigDecimal.valueOf(50.0),
                1L
        );

        when(dataSource.saveOrder(any(OrderDto.class)))
                .thenReturn(savedOrderDto);

        // Act
        Order result = gateway.save(order);

        // Assert
        assertNotNull(result);
        assertEquals(99L, result.getId());
    }


    @Test
    void findById_shouldReturnNull_whenOrderNotFound() {
        when(dataSource.findOrderById(1L)).thenReturn(null);

        Order result = gateway.findById(1L);

        assertNull(result);
        verify(dataSource, times(1)).findOrderById(1L);
    }
}
