package com.soat_tech_challenge4.app_order.api.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
}