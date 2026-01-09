package com.soat_tech_challenge4.app_order.api.data.jpa;

import com.soat_tech_challenge4.app_order.core.entities.Order;
import com.soat_tech_challenge4.app_order.core.entities.OrderItem;
import com.soat_tech_challenge4.app_order.core.entities.OrderStatusEnum;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderEntityTest {

    @Test
    void fromEntity_shouldMapOrderToOrderEntityCorrectly() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();

        OrderItem item1 = new OrderItem(1L, "d0e950f4-8249-4ea6-95eb-7637e98000c9", 2, BigDecimal.valueOf(5));
        OrderItem item2 = new OrderItem(2L, "2253b564-e668-41ad-b792-b49df48392bb", 1, BigDecimal.valueOf(10));

        Order order = new Order(
                99L,
                now,
                OrderStatusEnum.RECEBIDO,
                List.of(item1, item2),
                BigDecimal.valueOf(20),
                123L
        );

        // Act
        OrderEntity entity = OrderEntity.fromEntity(order);

        // Assert
        assertNotNull(entity);
        assertEquals(99L, entity.getId());
        assertEquals(now, entity.getOrderDate());
        assertEquals(OrderStatusEnum.RECEBIDO, entity.getOrderStatus());
        assertEquals(BigDecimal.valueOf(20), entity.getTotal());
        assertEquals(123L, entity.getPaymentId());

        assertNotNull(entity.getItems());
        assertEquals(2, entity.getItems().size());

        entity.getItems().forEach(itemEntity -> {
            assertNotNull(itemEntity.getOrder(), "Order reference must be set");
            assertSame(entity, itemEntity.getOrder(), "OrderItemEntity must reference parent OrderEntity");
            assertNotNull(itemEntity.getProductId());
            assertNotNull(itemEntity.getQuantity());
        });
    }
}
