package com.soat_tech_challenge4.app_order.core.interfaces;

import com.soat_tech_challenge4.app_order.core.entities.Product;

public interface IProductGateway {
    Product findById(Long id);
}
