package com.soat_tech_challenge4.app_order.api.rest.controller;

import com.soat_tech_challenge4.app_order.api.data.DataRepository;
import com.soat_tech_challenge4.app_order.api.rest.dto.request.OrderRequestDto;
import com.soat_tech_challenge4.app_order.core.controllers.OrderController;
import com.soat_tech_challenge4.app_order.core.dtos.OrderDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderItemDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderItemResponseDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderResponseDto;
import com.soat_tech_challenge4.app_order.core.entities.OrderStatusEnum;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderResourceTest {

    private OrderResource createResource(DataRepository repo) {
        return new OrderResource(repo);
    }

    private OrderItemDto createItem() {
        return new OrderItemDto(
                1L,               // productId
                "d0e950f4-8249-4ea6-95eb-7637e98000c9",              // categoryId
                1,                // quantity
                BigDecimal.TEN    // unitPrice
        );
    }

    private OrderDto createOrderDto() {
        return new OrderDto(
                1L,
                LocalDateTime.now(),
                OrderStatusEnum.CREATED,
                List.of(createItem()),
                BigDecimal.TEN,
                99L
        );
    }

    private OrderResponseDto createResponse() {
        return new OrderResponseDto(
                1L,
                LocalDateTime.now(),
                OrderStatusEnum.CREATED,
                List.of(new OrderItemResponseDto("d0e950f4-8249-4ea6-95eb-7637e98000c9", 1, BigDecimal.TEN)),
                BigDecimal.TEN,
                99L
        );
    }

    @Test
    void checkout_DeveRetornarCreated() {
        DataRepository repo = mock(DataRepository.class);
        OrderResource resource = createResource(repo);

        OrderRequestDto request = new OrderRequestDto(List.of(createItem()));
        OrderResponseDto expected = createResponse();

        try (MockedConstruction<OrderController> mocked =
                     mockConstruction(OrderController.class, (mock, context) ->
                             when(mock.checkout(request)).thenReturn(expected))) {

            ResponseEntity<OrderResponseDto> response = resource.checkout(request);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(expected, response.getBody());

            verify(mocked.constructed().get(0)).checkout(request);
        }
    }

    @Test
    void getOrders_DeveRetornarOk_QuandoListaNaoForNull() {
        DataRepository repo = mock(DataRepository.class);
        OrderResource resource = createResource(repo);

        List<OrderDto> lista = List.of(createOrderDto());

        try (MockedConstruction<OrderController> mocked =
                     mockConstruction(OrderController.class, (mock, context) ->
                             when(mock.getAllOrdersSorted()).thenReturn(lista))) {

            ResponseEntity<List<OrderDto>> response = resource.getOrders();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(lista, response.getBody());

            verify(mocked.constructed().get(0)).getAllOrdersSorted();
        }
    }

    @Test
    void getOrders_DeveRetornarNoContent_QuandoListaForNull() {
        DataRepository repo = mock(DataRepository.class);
        OrderResource resource = createResource(repo);

        try (MockedConstruction<OrderController> mocked =
                     mockConstruction(OrderController.class, (mock, context) ->
                             when(mock.getAllOrdersSorted()).thenReturn(null))) {

            ResponseEntity<List<OrderDto>> response = resource.getOrders();

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            assertNull(response.getBody());

            verify(mocked.constructed().get(0)).getAllOrdersSorted();
        }
    }

    @Test
    void getOrderById_DeveRetornarOk_QuandoEncontrado() {
        DataRepository repo = mock(DataRepository.class);
        OrderResource resource = createResource(repo);

        Long id = 1L;
        OrderDto dto = createOrderDto();

        try (MockedConstruction<OrderController> mocked =
                     mockConstruction(OrderController.class, (mock, context) ->
                             when(mock.findById(id)).thenReturn(dto))) {

            ResponseEntity<OrderDto> response = resource.getOrderById(id);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(dto, response.getBody());

            verify(mocked.constructed().get(0)).findById(id);
        }
    }

    @Test
    void getOrderById_DeveRetornarNotFound_QuandoNull() {
        DataRepository repo = mock(DataRepository.class);
        OrderResource resource = createResource(repo);

        Long id = 1L;

        try (MockedConstruction<OrderController> mocked =
                     mockConstruction(OrderController.class, (mock, context) ->
                             when(mock.findById(id)).thenReturn(null))) {

            ResponseEntity<OrderDto> response = resource.getOrderById(id);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

            verify(mocked.constructed().get(0)).findById(id);
        }
    }

    @Test
    void updateStatus_DeveRetornarOk_QuandoAtualizado() {
        DataRepository repo = mock(DataRepository.class);
        OrderResource resource = createResource(repo);

        Long id = 1L;
        String status = "PAID";
        OrderDto dto = createOrderDto();

        try (MockedConstruction<OrderController> mocked =
                     mockConstruction(OrderController.class, (mock, context) ->
                             when(mock.updateStatus(id, status)).thenReturn(dto))) {

            ResponseEntity<OrderDto> response = resource.updateStatus(id, status);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(dto, response.getBody());

            verify(mocked.constructed().get(0)).updateStatus(id, status);
        }
    }

    @Test
    void updateStatus_DeveRetornarNotFound_QuandoNull() {
        DataRepository repo = mock(DataRepository.class);
        OrderResource resource = createResource(repo);

        Long id = 1L;
        String status = "PAID";

        try (MockedConstruction<OrderController> mocked =
                     mockConstruction(OrderController.class, (mock, context) ->
                             when(mock.updateStatus(id, status)).thenReturn(null))) {

            ResponseEntity<OrderDto> response = resource.updateStatus(id, status);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

            verify(mocked.constructed().get(0)).updateStatus(id, status);
        }
    }
}