package ru.clevertec.check.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.clevertec.check.dto.ProductDTO;
import ru.clevertec.check.dto.mapper.ProductMapper;
import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.exception.ObjectNotFoundException;
import ru.clevertec.check.repository.api.ProductRepository;
import ru.clevertec.check.services.api.ProductService;

import java.util.Optional;

import static org.postgresql.hostchooser.HostRequirement.any;

class ProductServiceBaseTest {
    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final ProductService productServiceBase = new ProductServiceBase(productRepository, new ProductMapper());

    @Test
    void getProductNegativeCase() {
        Mockito.when(productRepository.findById(any.ordinal())).thenReturn(Optional.empty());
        CheckRunnerException thrown = Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            productServiceBase.getProduct("1");
        });
        Assertions.assertEquals(ObjectNotFoundException.class, thrown.getClass());
    }

    @Test
    void updateProductNegativeCase() {
        Mockito.when(productRepository.exists(any.ordinal())).thenReturn(false);
        CheckRunnerException thrown = Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            productServiceBase.updateProduct("1", new ProductDTO());
        });
        Assertions.assertEquals(ObjectNotFoundException.class, thrown.getClass());
    }

    @Test
    void deleteProductNegativeCase() {
        Mockito.when(productRepository.exists(any.ordinal())).thenReturn(false);
        CheckRunnerException thrown = Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            productServiceBase.deleteProduct("1");
        });
        Assertions.assertEquals(ObjectNotFoundException.class, thrown.getClass());
    }
}