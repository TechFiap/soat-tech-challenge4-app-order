package com.soat_tech_challenge4.app_order.core.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void allArgsConstructor_shouldAssignAllFieldsCorrectly() {
        OrderItem item = new OrderItem(
                1L,
                "d0e950f4-8249-4ea6-95eb-7637e98000c9",
                2,
                BigDecimal.valueOf(5)
        );

        assertEquals(1L, item.getId());
        assertEquals("d0e950f4-8249-4ea6-95eb-7637e98000c9", item.getProductId());
        assertEquals(2, item.getQuantity());
        assertEquals(BigDecimal.valueOf(5), item.getPrice());
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyObject() {
        OrderItem item = new OrderItem();

        assertNull(item.getId());
        assertNull(item.getProductId());
        assertNull(item.getQuantity());
        assertNull(item.getPrice());
    }

    @Test
    void constructorWithValidation_shouldCreateOrderItemWhenValid() {
        OrderItem item = new OrderItem(
                "d0e950f4-8249-4ea6-95eb-7637e98000c9",
                3,
                BigDecimal.valueOf(7.5)
        );

        assertEquals("d0e950f4-8249-4ea6-95eb-7637e98000c9", item.getProductId());
        assertEquals(3, item.getQuantity());
        assertEquals(BigDecimal.valueOf(7.5), item.getPrice());
    }

    @Test
    void constructorWithValidation_shouldThrowException_whenProductIdIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new OrderItem(null, 1, BigDecimal.TEN)
        );

        assertEquals("productId cannot be null or empty", exception.getMessage());
    }

    @Test
    void constructorWithValidation_shouldThrowException_whenQuantityIsZeroOrNegative() {
        IllegalArgumentException exceptionZero = assertThrows(
                IllegalArgumentException.class,
                () -> new OrderItem("d0e950f4-8249-4ea6-95eb-7637e98000c9", 0, BigDecimal.TEN)
        );

        IllegalArgumentException exceptionNegative = assertThrows(
                IllegalArgumentException.class,
                () -> new OrderItem("d0e950f4-8249-4ea6-95eb-7637e98000c9", -1, BigDecimal.TEN)
        );

        assertEquals("quantity must be greater than zero", exceptionZero.getMessage());
        assertEquals("quantity must be greater than zero", exceptionNegative.getMessage());
    }

    @Test
    void constructorWithValidation_shouldThrowException_whenPriceIsNullOrZeroOrNegative() {
        IllegalArgumentException exceptionNull = assertThrows(
                IllegalArgumentException.class,
                () -> new OrderItem("d0e950f4-8249-4ea6-95eb-7637e98000c9", 1, null)
        );

        IllegalArgumentException exceptionZero = assertThrows(
                IllegalArgumentException.class,
                () -> new OrderItem("d0e950f4-8249-4ea6-95eb-7637e98000c9", 1, BigDecimal.ZERO)
        );

        IllegalArgumentException exceptionNegative = assertThrows(
                IllegalArgumentException.class,
                () -> new OrderItem("d0e950f4-8249-4ea6-95eb-7637e98000c9", 1, BigDecimal.valueOf(-1))
        );

        assertEquals("price must be greater than zero", exceptionNull.getMessage());
        assertEquals("price must be greater than zero", exceptionZero.getMessage());
        assertEquals("price must be greater than zero", exceptionNegative.getMessage());
    }

    @Test
    void getSubtotal_shouldReturnPriceTimesQuantity() {
        OrderItem item = new OrderItem(
                "d0e950f4-8249-4ea6-95eb-7637e98000c9",
                4,
                BigDecimal.valueOf(2.5)
        );

        BigDecimal subtotal = item.getSubtotal();

        assertEquals(BigDecimal.valueOf(10.0), subtotal);
    }
}
