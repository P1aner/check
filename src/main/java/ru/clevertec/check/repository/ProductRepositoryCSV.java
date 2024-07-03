package ru.clevertec.check.repository;

import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.repository.api.ProductRepository;
import ru.clevertec.check.utils.CsvUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.clevertec.check.config.AppConfig.CSV_DELIMITER;
import static ru.clevertec.check.config.AppConfig.PATH_TO_PRODUCT_FILE;

public class ProductRepositoryCSV implements ProductRepository {

    private final List<Product> productList = new ArrayList<>();

    private static ProductRepositoryCSV instance;

    private ProductRepositoryCSV() {
    }

    public static ProductRepositoryCSV getInstance() {
        if (instance == null) {
            instance = new ProductRepositoryCSV();
            instance.productList.addAll(setUp());
        }
        return instance;
    }

    @Override
    public Optional<Product> findById(long id) {
        return productList.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    private static List<Product> setUp() {
        List<List<String>> lists = CsvUtil.readFromCSV(PATH_TO_PRODUCT_FILE, CSV_DELIMITER);
        List<Product> products = new ArrayList<>();
        try {
            for (int i = 1; i < lists.size(); i++) {
                List<String> list = lists.get(i);
                products.add(builProduct(list));
            }
        } catch (Exception e) {
            throw new CheckRunnerException("INTERNAL SERVER ERROR");
        }
        return products;
    }

    private static Product builProduct(List<String> list) {
        long id = Long.parseLong(list.get(0));
        String description = list.get(1);
        BigDecimal price = new BigDecimal(list.get(2));
        Integer quantityInStock = Integer.parseInt(list.get(3));
        boolean wholesaleProduct = Boolean.parseBoolean(list.get(4));
        return new Product(id, description, price, quantityInStock, wholesaleProduct);
    }
}