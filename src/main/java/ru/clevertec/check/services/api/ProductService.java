package ru.clevertec.check.services.api;

import ru.clevertec.check.dto.ProductDTO;

public interface ProductService {
    void createNewProduct(ProductDTO productDTO);

    ProductDTO getProduct(String id);

    void updateProduct(String id, ProductDTO productDTO);

    void deleteProduct(String id);
}
