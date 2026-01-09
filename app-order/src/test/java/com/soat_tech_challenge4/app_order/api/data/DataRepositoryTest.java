package com.soat_tech_challenge4.app_order.api.data;

import com.soat_tech_challenge4.app_order.api.data.jpa.OrderEntity;
import com.soat_tech_challenge4.app_order.api.data.jpa.OrderItemEntity;
import com.soat_tech_challenge4.app_order.api.data.jpa.OrderJpaRepository;
import com.soat_tech_challenge4.app_order.api.rest.client.ProductApiClientImpl;
import com.soat_tech_challenge4.app_order.api.rest.dto.response.ExternalProductResponse;
import com.soat_tech_challenge4.app_order.core.dtos.OrderDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderItemDto;
import com.soat_tech_challenge4.app_order.core.dtos.ProductDto;
import com.soat_tech_challenge4.app_order.core.entities.Category;
import com.soat_tech_challenge4.app_order.core.entities.OrderStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DataRepositoryTest {

    private OrderJpaRepository orderJpaRepository;
    private ProductApiClientImpl productApiClient;
    private DataRepository dataRepository;

    @BeforeEach
    void setup() {
        orderJpaRepository = mock(OrderJpaRepository.class);
        productApiClient = mock(ProductApiClientImpl.class);
        dataRepository = new DataRepository(orderJpaRepository, productApiClient);
    }

    // -------------------------------------------------------------
    // findOrderById
    // -------------------------------------------------------------
    @Test
    void testFindOrderById_ReturnsOrderDto() {
        OrderEntity entity = new OrderEntity();
        entity.setId(1L);
        entity.setOrderDate(LocalDateTime.now());
        entity.setOrderStatus(OrderStatusEnum.valueOf("CREATED"));
        entity.setTotal(BigDecimal.TEN);
        entity.setPaymentId(1L);

        OrderItemEntity item = new OrderItemEntity();
        item.setId(10L);
        item.setProductId("d0e950f4-8249-4ea6-95eb-7637e98000c9");
        item.setQuantity(2);
        item.setPrice(BigDecimal.valueOf(20));
        entity.setItems(List.of(item));

        when(orderJpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        OrderDto result = dataRepository.findOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(1, result.listOrderItemDto().size());
        assertEquals("d0e950f4-8249-4ea6-95eb-7637e98000c9", result.listOrderItemDto().get(0).productId());
    }

    @Test
    void testFindOrderById_ReturnsNullWhenNotFound() {
        when(orderJpaRepository.findById(1L)).thenReturn(Optional.empty());

        assertNull(dataRepository.findOrderById(1L));
    }

    // -------------------------------------------------------------
    // getAllOrders
    // -------------------------------------------------------------
    @Test
    void testGetAllOrders_ReturnsList() {
        OrderEntity entity = new OrderEntity();
        entity.setId(1L);
        entity.setOrderDate(LocalDateTime.now());
        entity.setOrderStatus(OrderStatusEnum.valueOf("CREATED"));
        entity.setTotal(BigDecimal.TEN);
        entity.setPaymentId(1L);

        OrderItemEntity item = new OrderItemEntity();
        item.setId(10L);
        item.setProductId("d0e950f4-8249-4ea6-95eb-7637e98000c9");
        item.setQuantity(2);
        item.setPrice(BigDecimal.valueOf(20));
        entity.setItems(List.of(item));

        when(orderJpaRepository.findAll()).thenReturn(List.of(entity));

        List<OrderDto> result = dataRepository.getAllOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
    }

    // -------------------------------------------------------------
    // saveOrder
    // -------------------------------------------------------------
    @Test
    void testSaveOrder_SavesCorrectly() {
        OrderItemDto itemDto = new OrderItemDto(10L, "d0e950f4-8249-4ea6-95eb-7637e98000c9", 2, BigDecimal.valueOf(20));
        OrderDto dto = new OrderDto(
                1L,
                LocalDateTime.now(),
                OrderStatusEnum.valueOf("CREATED"),
                List.of(itemDto),
                BigDecimal.TEN,
                1L
        );

        OrderEntity savedEntity = new OrderEntity();
        savedEntity.setId(1L);
        savedEntity.setOrderDate(dto.orderDate());
        savedEntity.setOrderStatus(dto.orderStatus());
        savedEntity.setTotal(dto.total());
        savedEntity.setPaymentId(dto.paymentId());
        savedEntity.setItems(List.of());

        when(orderJpaRepository.save(any(OrderEntity.class))).thenReturn(savedEntity);

        OrderDto result = dataRepository.saveOrder(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(dto.listOrderItemDto(), result.listOrderItemDto());

        ArgumentCaptor<OrderEntity> captor = ArgumentCaptor.forClass(OrderEntity.class);
        verify(orderJpaRepository).save(captor.capture());

        OrderEntity captured = captor.getValue();
        assertEquals(1, captured.getItems().size());
        assertEquals("d0e950f4-8249-4ea6-95eb-7637e98000c9", captured.getItems().get(0).getProductId());
    }

    // -------------------------------------------------------------
    // findById (Product)
    // -------------------------------------------------------------
    @Test
    void testFindById_ReturnsProductDto() {
        ExternalProductResponse response = new ExternalProductResponse(
                "d0e950f4-8249-4ea6-95eb-7637e98000c9",
                "Product",
                "Desc",
                BigDecimal.TEN,
                Category.BEBIDA,
                true
        );

        when(productApiClient.getByDocument("d0e950f4-8249-4ea6-95eb-7637e98000c9")).thenReturn(response);

        ProductDto result = dataRepository.findById("d0e950f4-8249-4ea6-95eb-7637e98000c9");

        assertNotNull(result);
        assertEquals("d0e950f4-8249-4ea6-95eb-7637e98000c9", result.id());
        assertEquals("Product", result.name());
    }

    @Test
    void testFindById_ReturnsNullWhenApiReturnsNull() {
        when(productApiClient.getByDocument("1")).thenReturn(null);

        assertNull(dataRepository.findById("d0e950f4-8249-4ea6-95eb-7637e98000c9"));
    }
}