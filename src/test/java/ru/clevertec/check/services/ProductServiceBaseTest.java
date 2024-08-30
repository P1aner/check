package ru.clevertec.check.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.clevertec.check.dto.ProductDTO;
import ru.clevertec.check.dto.mapper.ProductMapper;
import ru.clevertec.check.exception.ObjectNotFoundException;
import ru.clevertec.check.repository.api.ProductRepository;
import ru.clevertec.check.services.api.ProductService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

class ProductServiceBaseTest {
    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final ProductService productServiceBase = new ProductServiceBase(productRepository, new ProductMapper());

    @Test
    void getProductNegativeCase() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> productServiceBase.getProduct("1"));
    }

    @Test
    void updateProductPositiveCase() {
        Mockito.when(productRepository.exists(1L)).thenReturn(true);
        productServiceBase.updateProduct("1", new ProductDTO());
        Mockito.verify(productRepository, Mockito.times(1)).save(any());
    }

    @Test
    void updateProductNegativeCase() {
        Mockito.when(productRepository.exists(1L)).thenReturn(false);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> productServiceBase.updateProduct("1", null));
    }

    @Test
    void deleteProductPositiveCase() {
        Mockito.when(productRepository.exists(1L)).thenReturn(true);
        productServiceBase.deleteProduct("1");
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void deleteProductNegativeCase() {
        Mockito.when(productRepository.exists(1L)).thenReturn(false);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> productServiceBase.deleteProduct("1"));
    }

    @Test
    void createNewProduct() {
        productServiceBase.createNewProduct(new ProductDTO("desc", 0.1, 12, false));
        Mockito.verify(productRepository, Mockito.times(1)).save(any());
    }
}