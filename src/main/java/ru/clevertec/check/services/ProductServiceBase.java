package ru.clevertec.check.services;

import ru.clevertec.check.dto.ProductDTO;
import ru.clevertec.check.dto.mapper.ProductMapper;
import ru.clevertec.check.exception.ObjectNotFoundException;
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
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Product with id: %s not found", id)));
        return productMapper.producttoProductDTO(product);
    }

    public void updateProduct(String id, ProductDTO productDTO) {
        long parsedId = Long.parseLong(id);
        if (productRepository.exists(parsedId)) {
            Product product = productMapper.productDTOtoProduct(productDTO);
            product.setId(parsedId);
            productRepository.save(product);
        }
        throw new ObjectNotFoundException("Product %s not found".formatted(id));

    }

    public void deleteProduct(String id) {
        long parsedId = Long.parseLong(id);
        if (productRepository.exists(parsedId)) {
            productRepository.deleteById(parsedId);
        }
        throw new ObjectNotFoundException("Product %s not found".formatted(id));
    }
}