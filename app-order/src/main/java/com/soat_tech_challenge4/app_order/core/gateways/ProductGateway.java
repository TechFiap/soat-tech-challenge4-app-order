package com.soat_tech_challenge4.app_order.core.gateways;

import com.soat_tech_challenge4.app_order.core.dtos.ProductDto;
import com.soat_tech_challenge4.app_order.core.entities.Product;
import com.soat_tech_challenge4.app_order.core.interfaces.DataSource;
import com.soat_tech_challenge4.app_order.core.interfaces.IProductGateway;

public class ProductGateway implements IProductGateway {

    private final DataSource dataSource;

    public ProductGateway(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Product findById(String id) {
        ProductDto productDto = dataSource.findById(id);
        if (productDto == null) return null;

        return new Product(productDto.id(), productDto.name(), productDto.description(), productDto.price(), productDto.category(), productDto.avaliable());
    }

}