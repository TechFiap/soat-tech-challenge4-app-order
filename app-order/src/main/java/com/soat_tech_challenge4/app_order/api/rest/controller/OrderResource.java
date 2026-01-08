package com.soat_tech_challenge4.app_order.api.rest.controller;

import com.soat_tech_challenge4.app_order.api.data.DataRepository;
import com.soat_tech_challenge4.app_order.api.rest.dto.request.OrderRequestDto;
import com.soat_tech_challenge4.app_order.core.controllers.OrderController;
import com.soat_tech_challenge4.app_order.core.dtos.OrderDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderResource {

    private final DataRepository dataRepository;

    public OrderResource(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDto> checkout(@RequestBody OrderRequestDto orderRequestDto) {
        OrderController orderController = new OrderController(dataRepository);

        OrderResponseDto order = orderController.checkout(orderRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders() {
        OrderController orderController = new OrderController(dataRepository);

        List<OrderDto> result = orderController.getAllOrdersSorted();

        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
        // TODO: implement id validation
        OrderController orderController = new OrderController(dataRepository);
        OrderDto response = orderController.findById(orderId);

        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<OrderDto> updateStatus(@PathVariable Long orderId, @PathVariable String orderStatus) {
        OrderController orderController = new OrderController(dataRepository);

        OrderDto response = orderController.updateStatus(orderId, orderStatus);

        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
}
