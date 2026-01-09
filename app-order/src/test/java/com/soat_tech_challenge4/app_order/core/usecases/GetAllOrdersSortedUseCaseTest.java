
package com.soat_tech_challenge4.app_order.core.usecases;

import com.soat_tech_challenge4.app_order.core.entities.Order;
import com.soat_tech_challenge4.app_order.core.entities.OrderStatusEnum;
import com.soat_tech_challenge4.app_order.core.gateways.OrderGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAllOrdersSortedUseCaseTest {

    private OrderGateway orderGateway;
    private GetAllOrdersSortedUseCase useCase;

    @BeforeEach
    void setup() {
        orderGateway = mock(OrderGateway.class);
        useCase = new GetAllOrdersSortedUseCase(orderGateway);
    }

    @Test
    void testExecute_FiltersOutFinalizado() {
        Order o1 = mock(Order.class);
        when(o1.getOrderStatus()).thenReturn(OrderStatusEnum.FINALIZADO);

        Order o2 = mock(Order.class);
        when(o2.getOrderStatus()).thenReturn(OrderStatusEnum.RECEBIDO);
        when(o2.getOrderDate()).thenReturn(LocalDateTime.now());

        when(orderGateway.getAllOrders()).thenReturn(List.of(o1, o2));

        List<Order> result = useCase.execute();

        assertEquals(1, result.size());
        assertEquals(OrderStatusEnum.RECEBIDO, result.getFirst().getOrderStatus());
    }

    @Test
    void testExecute_SortsByPriorityThenDate() {
        LocalDateTime now = LocalDateTime.now();

        Order recebido1 = mock(Order.class);
        when(recebido1.getOrderStatus()).thenReturn(OrderStatusEnum.RECEBIDO);
        when(recebido1.getOrderDate()).thenReturn(now.plusMinutes(10));

        Order recebido2 = mock(Order.class);
        when(recebido2.getOrderStatus()).thenReturn(OrderStatusEnum.RECEBIDO);
        when(recebido2.getOrderDate()).thenReturn(now);

        Order pronto = mock(Order.class);
        when(pronto.getOrderStatus()).thenReturn(OrderStatusEnum.PRONTO);
        when(pronto.getOrderDate()).thenReturn(now.plusMinutes(5));

        Order emPreparacao = mock(Order.class);
        when(emPreparacao.getOrderStatus()).thenReturn(OrderStatusEnum.EM_PREPARACAO);
        when(emPreparacao.getOrderDate()).thenReturn(now.plusMinutes(2));

        when(orderGateway.getAllOrders())
                .thenReturn(List.of(recebido1, recebido2, pronto, emPreparacao));

        List<Order> result = useCase.execute();

        assertEquals(4, result.size());

        // Ordem esperada:
        // 1) PRONTO (prioridade 1)
        // 2) EM_PREPARACAO (prioridade 2)
        // 3) RECEBIDO (prioridade 3, ordenado por data)
        // 4) RECEBIDO (mais tarde)
        assertEquals(pronto, result.get(0));
        assertEquals(emPreparacao, result.get(1));
        assertEquals(recebido2, result.get(2));
        assertEquals(recebido1, result.get(3));
    }

    @Test
    void testExecute_IgnoresOrdersWithNullStatus() {
        Order o1 = mock(Order.class);
        when(o1.getOrderStatus()).thenReturn(null);

        Order o2 = mock(Order.class);
        when(o2.getOrderStatus()).thenReturn(OrderStatusEnum.RECEBIDO);
        when(o2.getOrderDate()).thenReturn(LocalDateTime.now());

        when(orderGateway.getAllOrders()).thenReturn(List.of(o1, o2));

        List<Order> result = useCase.execute();

        assertEquals(1, result.size());
        assertEquals(OrderStatusEnum.RECEBIDO, result.get(0).getOrderStatus());
    }
}