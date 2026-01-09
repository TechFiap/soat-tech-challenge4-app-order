package com.soat_tech_challenge4.app_order.core.usecases;

import com.soat_tech_challenge4.app_order.api.rest.dto.request.OrderRequestDto;
import com.soat_tech_challenge4.app_order.application.exceptions.ErrorException;
import com.soat_tech_challenge4.app_order.core.dtos.OrderItemDto;
import com.soat_tech_challenge4.app_order.core.entities.Category;
import com.soat_tech_challenge4.app_order.core.entities.Order;
import com.soat_tech_challenge4.app_order.core.entities.OrderItem;
import com.soat_tech_challenge4.app_order.core.entities.Product;
import com.soat_tech_challenge4.app_order.core.gateways.OrderGateway;
import com.soat_tech_challenge4.app_order.core.gateways.ProductGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateOrderUseCaseTest {

    private OrderGateway orderGateway;
    private ProductGateway productGateway;
    private CreateOrderUseCase useCase;

    @BeforeEach
    void setup() {
        orderGateway = mock(OrderGateway.class);
        productGateway = mock(ProductGateway.class);
        useCase = new CreateOrderUseCase(orderGateway, productGateway);
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        // Arrange
        OrderItemDto itemDto = new OrderItemDto(1L, "d0e950f4-8249-4ea6-95eb-7637e98000c9", 5,BigDecimal.TEN);
        OrderRequestDto request = new OrderRequestDto(List.of(itemDto));

        Product product = new Product("d0e950f4-8249-4ea6-95eb-7637e98000c9", "Coca Cola 500ml", "Test Product", BigDecimal.TEN,
                Category.BEBIDA, true);
        when(productGateway.findById("d0e950f4-8249-4ea6-95eb-7637e98000c9")).thenReturn(product);

        Order savedOrder = new Order(List.of(new OrderItem("d0e950f4-8249-4ea6-95eb-7637e98000c9", 5,  BigDecimal.TEN)));
        when(orderGateway.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        Order result = useCase.execute(request);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals("d0e950f4-8249-4ea6-95eb-7637e98000c9", result.getItems().get(0).getProductId());
        assertEquals(5, result.getItems().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(10), result.getItems().get(0).getPrice());

        verify(productGateway, times(1)).findById("d0e950f4-8249-4ea6-95eb-7637e98000c9");

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderGateway).save(captor.capture());

        Order capturedOrder = captor.getValue();
        assertEquals(1, capturedOrder.getItems().size());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        // Arrange
        OrderItemDto itemDto = new OrderItemDto(1L, "d0e950f4-8249-4ea6-95eb-7637e98000c9", 5, BigDecimal.TEN);
        OrderRequestDto request = new OrderRequestDto(List.of(itemDto));

        when(productGateway.findById("d0e950f4-8249-4ea6-95eb-7637e98000c9")).thenReturn(null);

        // Act + Assert
        ErrorException ex = assertThrows(
                ErrorException.class,
                () -> useCase.execute(request)
        );

        assertEquals("Product with id d0e950f4-8249-4ea6-95eb-7637e98000c9 not found in database", ex.getMessage());
        verify(orderGateway, never()).save(any());
    }
}