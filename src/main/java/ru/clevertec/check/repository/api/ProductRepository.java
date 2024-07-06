package ru.clevertec.check.repository.api;

import ru.clevertec.check.model.Product;

import java.util.Collection;
import java.util.List;

public interface ProductRepository extends GenericRepository<Product> {

    List<Product> findByIds(Collection<Long> productIds);
}