package ru.clevertec.check.repository;

import ru.clevertec.check.model.Product;
import ru.clevertec.check.repository.api.ProductRepository;
import ru.clevertec.check.utils.CSVReader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static ru.clevertec.check.config.AppConfig.CSV_DELIMITER;
import static ru.clevertec.check.config.AppConfig.PATH_TO_PRODUCT_FILE;

public class ProductRepositoryImpl implements ProductRepository {
    private static final Logger logger = Logger.getLogger("ru.clevertec.check.CheckRunner");

    private final List<Product> productList = new ArrayList<>();

    private static ProductRepositoryImpl instance;

    private ProductRepositoryImpl() {
    }

    public static ProductRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new ProductRepositoryImpl();
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
        List<List<String>> lists = new CSVReader().readFromCSV(PATH_TO_PRODUCT_FILE, CSV_DELIMITER);
        List<Product> products = new ArrayList<>();
        for (int i = 1; i < lists.size(); i++) {
            List<String> list = lists.get(i);
            try {
                products.add(new Product(Long.parseLong(list.get(0)), list.get(1), new BigDecimal(list.get(2)), Integer.parseInt(list.get(3)), list.get(4).equals("+")));
            } catch (Exception e) {
                logger.warning("INTERNAL SERVER ERROR");
                throw new RuntimeException();
            }
        }
        return products;
    }
}