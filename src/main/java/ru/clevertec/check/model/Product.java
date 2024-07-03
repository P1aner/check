package ru.clevertec.check.model;

import java.math.BigDecimal;

public class Product {
    private Long id;
    private String description;
    private BigDecimal price;
    private Integer quantityInStock;
    private boolean wholesaleProduct;

    public Product(Long id, String description, BigDecimal price, Integer quantityInStock, boolean wholesaleProduct) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.wholesaleProduct = wholesaleProduct;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }


    public void setWholesaleProduct(Boolean wholesaleProduct) {
        this.wholesaleProduct = wholesaleProduct;
    }

    public boolean isWholesaleProduct() {
        return wholesaleProduct;
    }
}