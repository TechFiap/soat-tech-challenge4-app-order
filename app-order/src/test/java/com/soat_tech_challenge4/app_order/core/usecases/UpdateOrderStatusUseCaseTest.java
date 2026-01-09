package com.soat_tech_challenge4.app_order.core.usecases;

import com.soat_tech_challenge4.app_order.application.exceptions.ErrorException;
import com.soat_tech_challenge4.app_order.core.entities.Order;
import com.soat_tech_challenge4.app_order.core.entities.OrderItem;
import com.soat_tech_challenge4.app_order.core.entities.OrderStatusEnum;
import com.soat_tech_challenge4.app_order.core.gateways.OrderGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateOrderStatusUseCaseTest {

    private OrderGateway orderGateway;
    private UpdateOrderStatusUseCase useCase;

    @BeforeEach
    void setUp() {
        orderGateway = mock(OrderGateway.class);
        useCase = new UpdateOrderStatusUseCase(orderGateway);
    }

    @Test
    void execute_shouldUpdateOrderStatusAndSave_whenOrderExists() {
        // Arrange
        Order order = new Order(10L, LocalDateTime.now(), OrderStatusEnum.CREATED,
                    List.of(new OrderItem("d0e950f4-8249-4ea6-95eb-7637e98000c9", 1, BigDecimal.valueOf(15.0))),
                    BigDecimal.valueOf(15.0), 1L);

        when(orderGateway.findById(10L)).thenReturn(order);
        when(orderGateway.save(order)).thenReturn(order);

        // Act
        Order result = useCase.execute(10L, "RECEBIDO");

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatusEnum.valueOf("RECEBIDO"), result.getOrderStatus());

        verify(orderGateway, times(1)).findById(10L);
        verify(orderGateway, times(1)).save(order);
    }

    @Test
    void execute_shouldThrowException_whenOrderDoesNotExist() {
        // Arrange
        when(orderGateway.findById(99L)).thenReturn(null);

        // Act + Assert
        ErrorException exception = assertThrows(
                ErrorException.class,
                () -> useCase.execute(99L, "PAID")
        );

        assertEquals("Order not found with id 99", exception.getMessage());

        verify(orderGateway, times(1)).findById(99L);
        verify(orderGateway, never()).save(any());
    }
}
