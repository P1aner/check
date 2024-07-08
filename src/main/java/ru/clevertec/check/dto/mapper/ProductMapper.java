package ru.clevertec.check.dto.mapper;

import ru.clevertec.check.dto.ProductDTO;
import ru.clevertec.check.model.Product;

import java.math.BigDecimal;

public class ProductMapper {
    public Product productDTOtoProduct(ProductDTO productDTO) {
        String description = productDTO.getDescription();
        double price = productDTO.getPrice();
        int quantity = productDTO.getQuantity();
        boolean wholesale = productDTO.getIsWholesale();
        return new Product(description, BigDecimal.valueOf(price), quantity, wholesale);
    }

    public ProductDTO producttoProductDTO(Product product) {
        String description = product.getDescription();
        double price = Double.parseDouble(String.valueOf(product.getPrice()));
        int quantity = product.getQuantityInStock();
        boolean wholesale = product.isWholesaleProduct();
        return new ProductDTO(description, price, quantity, wholesale);
    }
}
