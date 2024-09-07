package ru.clevertec.check.services.data;

import ru.clevertec.check.dto.ProductDTO;

public class ProductDTOTestData {

    public static ProductDTO getProductDTOWithoutId() {
        return new ProductDTO("desc", 10.10, 12, true);
    }
}
