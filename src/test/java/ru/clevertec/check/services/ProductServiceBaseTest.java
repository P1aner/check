package ru.clevertec.check.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.clevertec.check.dto.ProductDTO;
import ru.clevertec.check.dto.mapper.ProductMapper;
import ru.clevertec.check.exception.ObjectNotFoundException;
import ru.clevertec.check.repository.api.ProductRepository;
import ru.clevertec.check.services.api.ProductService;
import ru.clevertec.check.services.data.ProductDTOTestData;
import ru.clevertec.check.services.data.ProductTestData;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

class ProductServiceBaseTest {
    private static final String NUMBER = "1";

    private final ProductRepository productRepositoryMock = Mockito.mock(ProductRepository.class);
    private final ProductMapper productMapperMock = Mockito.mock(ProductMapper.class);
    private final ProductService productServiceBase = new ProductServiceBase(productRepositoryMock, productMapperMock);


    @Test
    void getProductNegativeCase() {
        Mockito.when(productRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ObjectNotFoundException.class, () -> productServiceBase.getProduct(NUMBER));
    }

    @Test
    void updateProductPositiveCase() {
        ProductDTO productDTO = ProductDTOTestData.getProductDTOWithoutId();

        Mockito.when(productRepositoryMock.exists(1L)).thenReturn(true);
        Mockito.when(productMapperMock.productDTOtoProduct(productDTO)).thenReturn(ProductTestData.getProductWithoutId());

        productServiceBase.updateProduct(NUMBER, productDTO);
        Mockito.verify(productRepositoryMock, Mockito.times(1)).save(any());
    }

    @Test
    void updateProductNegativeCase() {
        ProductDTO productDTO = ProductDTOTestData.getProductDTOWithoutId();

        Mockito.when(productRepositoryMock.exists(1L)).thenReturn(false);

        Assertions.assertThrows(ObjectNotFoundException.class, () -> productServiceBase.updateProduct(NUMBER, productDTO));
    }

    @Test
    void deleteProductPositiveCase() {
        Mockito.when(productRepositoryMock.exists(1L)).thenReturn(true);

        productServiceBase.deleteProduct(NUMBER);

        Mockito.verify(productRepositoryMock, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void deleteProductNegativeCase() {
        Mockito.when(productRepositoryMock.exists(1L)).thenReturn(false);

        Assertions.assertThrows(ObjectNotFoundException.class, () -> productServiceBase.deleteProduct(NUMBER));
    }

    @Test
    void createNewProduct() {
        productServiceBase.createNewProduct(ProductDTOTestData.getProductDTOWithoutId());

        Mockito.verify(productRepositoryMock, Mockito.times(1)).save(any());
    }
}