package com.soat_tech_challenge4.app_order.core.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testConstructorWithId_Success() {
        Product product = new Product(
                1L,
                "Coca-Cola",
                "Refrigerante",
                BigDecimal.valueOf(10),
                Category.BEBIDA,
                true
        );

        assertEquals(1L, product.getId());
        assertEquals("Coca-Cola", product.getName());
        assertEquals("Refrigerante", product.getDescription());
        assertEquals(BigDecimal.valueOf(10), product.getPrice());
        assertEquals(Category.BEBIDA, product.getCategory());
        assertTrue(product.getAvaliable());
    }

    @Test
    void testConstructorWithoutId_DefaultAvaliableTrue() {
        Product product = new Product(
                "Coca-Cola",
                "Refrigerante",
                BigDecimal.valueOf(10),
                Category.BEBIDA
        );

        assertNull(product.getId());
        assertEquals("Coca-Cola", product.getName());
        assertEquals("Refrigerante", product.getDescription());
        assertEquals(BigDecimal.valueOf(10), product.getPrice());
        assertEquals(Category.BEBIDA, product.getCategory());
        assertTrue(product.getAvaliable());
    }

    // -----------------------------
    // Validation tests
    // -----------------------------

    @Test
    void testValidate_NameNull_ThrowsException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Product(null, "desc", BigDecimal.ONE, Category.BEBIDA)
        );
        assertEquals("Product name cannot be null or empty", ex.getMessage());
    }

    @Test
    void testValidate_NameEmpty_ThrowsException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Product("", "desc", BigDecimal.ONE, Category.BEBIDA)
        );
        assertEquals("Product name cannot be null or empty", ex.getMessage());
    }

    @Test
    void testValidate_NameTooLong_ThrowsException() {
        String longName = "A".repeat(101);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Product(longName, "desc", BigDecimal.ONE, Category.BEBIDA)
        );
        assertEquals("Product name is too long. Max length is 100", ex.getMessage());
    }

    @Test
    void testValidate_DescriptionNull_ThrowsException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Product("Coca", null, BigDecimal.ONE, Category.BEBIDA)
        );
        assertEquals("Product description cannot be null or empty", ex.getMessage());
    }

    @Test
    void testValidate_DescriptionEmpty_ThrowsException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Product("Coca", "", BigDecimal.ONE, Category.BEBIDA)
        );
        assertEquals("Product description cannot be null or empty", ex.getMessage());
    }

    @Test
    void testValidate_PriceNull_ThrowsException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Product("Coca", "desc", null, Category.BEBIDA)
        );
        assertEquals("Product price must be not null", ex.getMessage());
    }

    @Test
    void testValidate_PriceZeroOrNegative_ThrowsException() {
        IllegalArgumentException ex1 = assertThrows(
                IllegalArgumentException.class,
                () -> new Product("Coca", "desc", BigDecimal.ZERO, Category.BEBIDA)
        );
        assertEquals("Product price must be greater than 0", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(
                IllegalArgumentException.class,
                () -> new Product("Coca", "desc", BigDecimal.valueOf(-5), Category.BEBIDA)
        );
        assertEquals("Product price must be greater than 0", ex2.getMessage());
    }
}