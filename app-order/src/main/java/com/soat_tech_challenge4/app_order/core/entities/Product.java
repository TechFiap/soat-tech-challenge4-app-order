package com.soat_tech_challenge4.app_order.core.entities;

import java.math.BigDecimal;

public class Product {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Category category;
    private Boolean avaliable;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public Boolean getAvaliable() {
        return avaliable;
    }

    public Product(Long id, String name, String description, BigDecimal price, Category category, Boolean avaliable){
        validate(name, description, price, category);
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.avaliable = avaliable;
    }

    public Product(String name, String description, BigDecimal price, Category category) {
        validate(name, description, price, category);
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.avaliable = true;
    }

    private void validate(String name, String description, BigDecimal price, Category category) throws IllegalArgumentException{
        if (name == null || name.isEmpty()){
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }

        if (name.length() > 100){
            throw new IllegalArgumentException("Product name is too long. Max length is 100");
        }

        if (description == null || description.isEmpty()){
            throw new IllegalArgumentException("Product description cannot be null or empty");
        }

        if (price == null){
            throw new IllegalArgumentException("Product price must be not null");
        }

        if (price.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Product price must be greater than 0");
        }
    }

}