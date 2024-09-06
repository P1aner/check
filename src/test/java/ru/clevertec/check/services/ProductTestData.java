package ru.clevertec.check.services;

import ru.clevertec.check.dto.ProductDTO;
import ru.clevertec.check.model.Product;

import java.math.BigDecimal;

public class ProductTestData {
    public static Product getProduct() {
        return new Product("desc", BigDecimal.valueOf(10.1), 12, true);
    }
    public static ProductDTO getProductDTO(){
        return new ProductDTO("desc", 10.1, 12, true);
    }

}
