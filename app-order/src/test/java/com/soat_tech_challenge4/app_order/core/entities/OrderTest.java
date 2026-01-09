package com.soat_tech_challenge4.app_order.core.entities;

import com.soat_tech_challenge4.app_order.application.exceptions.ErrorException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void constructorWithItems_shouldInitializeFieldsAndCalculateTotal() {
        OrderItem item1 = new OrderItem(1L, "d0e950f4-8249-4ea6-95eb-7637e98000c9", 2, BigDecimal.valueOf(5));
        OrderItem item2 = new OrderItem(2L, "2253b564-e668-41ad-b792-b49df48392bb", 1, BigDecimal.valueOf(10));

        Order order = new Order(List.of(item1, item2));

        assertNotNull(order.getOrderDate());
        assertEquals(OrderStatusEnum.CREATED, order.getOrderStatus());
        assertEquals(BigDecimal.valueOf(20), order.getTotal());
        assertEquals(2, order.getItems().size());
    }

    @Test
    void allArgsConstructor_shouldAssignAllFieldsCorrectly() {
        LocalDateTime now = LocalDateTime.now();

        OrderItem item = new OrderItem(1L, "d0e950f4-8249-4ea6-95eb-7637e98000c9", 1, BigDecimal.valueOf(15));

        Order order = new Order(
                5L,
                now,
                OrderStatusEnum.valueOf("RECEBIDO"),
                List.of(item),
                BigDecimal.valueOf(15),
                99L
        );

        assertEquals(5L, order.getId());
        assertEquals(now, order.getOrderDate());
        assertEquals(OrderStatusEnum.valueOf("RECEBIDO"), order.getOrderStatus());
        assertEquals(BigDecimal.valueOf(15), order.getTotal());
        assertEquals(99L, order.getPaymentId());
    }

    @Test
    void setId_shouldUpdateOrderId() {
        Order order = new Order(List.of(
                new OrderItem(1L, "d0e950f4-8249-4ea6-95eb-7637e98000c9", 1, BigDecimal.TEN)
        ));

        order.setId(100L);

        assertEquals(100L, order.getId());
    }

    @Test
    void setPaymentId_shouldUpdatePaymentId() {
        Order order = new Order(List.of(
                new OrderItem(1L, "d0e950f4-8249-4ea6-95eb-7637e98000c9", 1, BigDecimal.TEN)
        ));

        order.setPaymentId(55L);

        assertEquals(55L, order.getPaymentId());
    }

    @Test
    void calculateTotal_shouldReturnSumOfItemSubtotals() {
        OrderItem item1 = new OrderItem(1L, "d0e950f4-8249-4ea6-95eb-7637e98000c9", 2, BigDecimal.valueOf(10));
        OrderItem item2 = new OrderItem(2L, "2253b564-e668-41ad-b792-b49df48392bb", 3, BigDecimal.valueOf(5));

        Order order = new Order(List.of(item1, item2));

        BigDecimal total = order.calculateTotal();

        assertEquals(BigDecimal.valueOf(35), total);
    }

    @Test
    void updateStatus_shouldUpdateStatusWhenValidAndDifferent() {
        Order order = new Order(List.of(
                new OrderItem(1L, "d0e950f4-8249-4ea6-95eb-7637e98000c9", 1, BigDecimal.TEN)
        ));

        order.updateStatus("RECEBIDO");

        assertEquals(OrderStatusEnum.valueOf("RECEBIDO"), order.getOrderStatus());
    }

    @Test
    void updateStatus_shouldThrowIllegalArgumentException_whenStatusIsInvalid() {
        Order order = new Order(List.of(
                new OrderItem(1L, "d0e950f4-8249-4ea6-95eb-7637e98000c9", 1, BigDecimal.TEN)
        ));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> order.updateStatus("INVALID_STATUS")
        );

        assertEquals("Order status not recognized", exception.getMessage());
    }

    @Test
    void updateStatus_shouldThrowErrorException_whenStatusIsTheSame() {
        Order order = new Order(List.of(
                new OrderItem(1L, "d0e950f4-8249-4ea6-95eb-7637e98000c9", 1, BigDecimal.TEN)
        ));

        ErrorException exception = assertThrows(
                ErrorException.class,
                () -> order.updateStatus("CREATED")
        );

        assertEquals("Order status is the same", exception.getMessage());
    }
}
