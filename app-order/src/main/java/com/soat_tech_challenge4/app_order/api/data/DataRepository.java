package com.soat_tech_challenge4.app_order.api.data;

import com.soat_tech_challenge4.app_order.api.data.jpa.OrderEntity;
import com.soat_tech_challenge4.app_order.api.data.jpa.OrderItemEntity;
import com.soat_tech_challenge4.app_order.api.data.jpa.OrderJpaRepository;
import com.soat_tech_challenge4.app_order.api.rest.client.ProductApiClientImpl;
import com.soat_tech_challenge4.app_order.api.rest.dto.response.ExternalProductResponse;
import com.soat_tech_challenge4.app_order.core.dtos.OrderDto;
import com.soat_tech_challenge4.app_order.core.dtos.OrderItemDto;
import com.soat_tech_challenge4.app_order.core.dtos.ProductDto;
import com.soat_tech_challenge4.app_order.core.interfaces.DataSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DataRepository implements DataSource {

    private final OrderJpaRepository orderJpaRepository;
    private final ProductApiClientImpl productApiClient;

    public DataRepository(
            OrderJpaRepository orderJpaRepository,
            ProductApiClientImpl productApiClient
    ) {
        this.orderJpaRepository = orderJpaRepository;
        this.productApiClient = productApiClient;
    }

    //Recebe um DTO e transforma para Entity do JPA para salvar
    //Devolve um DTO
    @Override
    public OrderDto findOrderById(Long id) {
        Optional<OrderEntity> orderEntity = orderJpaRepository.findById(id);
        if (orderEntity.isEmpty()) return null;

        List<OrderItemDto> orderItemDtoList = orderEntity.get().getItems().stream()
                .map(orderItem -> new OrderItemDto(
                        orderItem.getId(),
                        orderItem.getProductId(),
                        orderItem.getQuantity(),
                        orderItem.getPrice()))
                .toList();

        return new OrderDto(
                orderEntity.get().getId(),
                orderEntity.get().getOrderDate(),
                orderEntity.get().getOrderStatus(),
                orderItemDtoList,
                orderEntity.get().getTotal(),
                orderEntity.get().getPaymentId());
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<OrderEntity> listOrderEntity = orderJpaRepository.findAll();

        return listOrderEntity.stream()
                .map(orderEntity -> {
                    List<OrderItemDto> itemDtos = orderEntity.getItems().stream()
                            .map(orderItem -> new OrderItemDto(
                                    orderItem.getId(),
                                    orderItem.getProductId(),
                                    orderItem.getQuantity(),
                                    orderItem.getPrice()))
                            .toList();

                    return new OrderDto(
                            orderEntity.getId(),
                            orderEntity.getOrderDate(),
                            orderEntity.getOrderStatus(),
                            itemDtos,
                            orderEntity.getTotal(),
                            orderEntity.getPaymentId()
                    );
                })
                .toList();
    }

    @Override
    public OrderDto saveOrder(OrderDto orderDto) {
        OrderEntity orderEntity = new OrderEntity();

        orderEntity.setId(orderDto.id());
        orderEntity.setOrderDate(orderDto.orderDate());
        orderEntity.setOrderStatus(orderDto.orderStatus());
        orderEntity.setTotal(orderDto.total());

        List<OrderItemEntity> orderItemEntityList = orderDto.listOrderItemDto().stream()
                .map(item -> {
                    OrderItemEntity itemEntity = new OrderItemEntity();
                    itemEntity.setProductId(item.productId());
                    itemEntity.setQuantity(item.quantity());
                    itemEntity.setPrice(item.price());
                    itemEntity.setOrder(orderEntity); // ESSENCIAL!
                    return itemEntity;
                }).toList();

        orderEntity.setItems(orderItemEntityList);

        OrderEntity savedEntity = orderJpaRepository.save(orderEntity);

        return new OrderDto(
                savedEntity.getId(),
                savedEntity.getOrderDate(),
                savedEntity.getOrderStatus(),
                orderDto.listOrderItemDto(),
                savedEntity.getTotal(),
                savedEntity.getPaymentId());
    }


    @Override
    public ProductDto findById(Long id) {
        ExternalProductResponse externalProductResponse = productApiClient.getByDocument(id.toString());

        if (externalProductResponse == null) return null;

        return new ProductDto(
                externalProductResponse.id(),
                externalProductResponse.name(),
                externalProductResponse.description(),
                externalProductResponse.price(),
                externalProductResponse.category(),
                externalProductResponse.avaliable());
    }
}
