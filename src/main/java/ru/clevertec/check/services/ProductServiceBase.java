package ru.clevertec.check.services;

import ru.clevertec.check.dto.ProductDTO;
import ru.clevertec.check.dto.mapper.ProductMapper;
import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.repository.ProductRepositorySqL;
import ru.clevertec.check.repository.api.ProductRepository;

public class ProductServiceBase {
    private final ProductRepository productRepository = new ProductRepositorySqL();
    private final ProductMapper productMapper = new ProductMapper();

    public void createNewProduct(ProductDTO productDTO) {
        Product product = productMapper.productDTOtoProduct(productDTO);
        productRepository.save(product);
    }

    public ProductDTO getProduct(String id) {
        long l = Long.parseLong(id);
        Product product = productRepository.findById(l)
                .orElseThrow(() -> new CheckRunnerException("INTERNAL SERVER ERROR"));
        return productMapper.producttoProductDTO(product);
    }

    public void updateProduct(ProductDTO productDTO, String id) {
        Product product = productMapper.productDTOtoProduct(productDTO);
        long l = Long.parseLong(id);
        product.setId(l);
        productRepository.save(product);
    }

    public void deleteProduct(String id) {
        long l = Long.parseLong(id);
        productRepository.deleteById(l);
    }
}