package com.soat_tech_challenge4.app_order.core.gateways;

import com.soat_tech_challenge4.app_order.core.dtos.ProductDto;
import com.soat_tech_challenge4.app_order.core.entities.Category;
import com.soat_tech_challenge4.app_order.core.entities.Product;
import com.soat_tech_challenge4.app_order.core.interfaces.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductGatewayTest {

    private DataSource dataSource;
    private ProductGateway productGateway;

    @BeforeEach
    void setUp() {
        dataSource = mock(DataSource.class);
        productGateway = new ProductGateway(dataSource);
    }

    @Test
    void findById_shouldReturnProduct_whenProductExists() {
        // Arrange
        ProductDto productDto = new ProductDto(
                10L,
                "Burger",
                "Cheeseburger",
                BigDecimal.valueOf(25),
                Category.LANCHE,
                true
        );

        when(dataSource.findById(10L)).thenReturn(productDto);

        // Act
        Product result = productGateway.findById(10L);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Burger", result.getName());
        assertEquals("Cheeseburger", result.getDescription());
        assertEquals(BigDecimal.valueOf(25), result.getPrice());
        assertEquals(Category.LANCHE, result.getCategory());
        assertTrue(result.getAvaliable());

        verify(dataSource, times(1)).findById(10L);
    }

    @Test
    void findById_shouldReturnNull_whenProductDoesNotExist() {
        // Arrange
        when(dataSource.findById(99L)).thenReturn(null);

        // Act
        Product result = productGateway.findById(99L);

        // Assert
        assertNull(result);
        verify(dataSource, times(1)).findById(99L);
    }
}
