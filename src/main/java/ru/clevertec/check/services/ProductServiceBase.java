package ru.clevertec.check.services;

import ru.clevertec.check.dto.ProductDTO;
import ru.clevertec.check.dto.mapper.ProductMapper;
import ru.clevertec.check.exception.ObjectNotFoundException;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.repository.api.ProductRepository;
import ru.clevertec.check.services.api.ProductService;

public class ProductServiceBase implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceBase(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public void createNewProduct(ProductDTO productDTO) {
        Product product = productMapper.productDTOtoProduct(productDTO);
        productRepository.save(product);
    }

    @Override
    public ProductDTO getProduct(String id) {
        long l = Long.parseLong(id);
        Product product = productRepository.findById(l)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Product with id: %s not found", id)));
        return productMapper.producttoProductDTO(product);
    }

    @Override
    public void updateProduct(String id, ProductDTO productDTO) {
        long parsedId = Long.parseLong(id);
        if (productRepository.exists(parsedId)) {
            Product product = productMapper.productDTOtoProduct(productDTO);
            product.setId(parsedId);
            productRepository.save(product);
        } else {
            throw new ObjectNotFoundException("Product %s not found".formatted(id));
        }
    }

    @Override
    public void deleteProduct(String id) {
        long parsedId = Long.parseLong(id);
        if (productRepository.exists(parsedId)) {
            productRepository.deleteById(parsedId);
        } else {
            throw new ObjectNotFoundException("Product %s not found".formatted(id));
        }
    }
}