package ru.clevertec.check.repository;

import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.repository.api.ProductRepository;
import ru.clevertec.check.utils.JDBCConnector;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ru.clevertec.check.utils.MapperFromResultSet.productMapper;

public class ProductRepositorySqL implements ProductRepository {
    public static final String SELECT_FROM_PRODUCT_WHERE_ID = "SELECT * FROM product WHERE id = ?";
    public static final String SELECT_FROM_PRODUCT_WHERE_ID_ANY = "SELECT * FROM product WHERE id = any (?)";
    private static ProductRepositorySqL instance;
    private final JDBCConnector connector;

    private ProductRepositorySqL(JDBCConnector connector) {
        this.connector = connector;
    }

    public static ProductRepositorySqL getInstance() {
        if (instance == null) {
            JDBCConnector connector = JDBCConnector.getInstance();
            instance = new ProductRepositorySqL(connector);
        }
        return instance;
    }

    @Override
    public Optional<Product> findById(long id) {
        Product product = null;
        Connection connection = connector.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_PRODUCT_WHERE_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                product = productMapper(resultSet);
            }
        } catch (SQLException e) {
            throw new CheckRunnerException("INTERNAL SERVER ERROR");
        }
        return Optional.ofNullable(product);
    }

    @Override
    public List<Product> findByIds(Collection<Long> productIds) {
        Connection connection = connector.getConnection();
        List<Product> products = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_PRODUCT_WHERE_ID_ANY)) {
            Array productIdsArray = connection.createArrayOf("BIGINT", productIds.toArray());
            preparedStatement.setArray(1, productIdsArray);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                products.add(productMapper(resultSet));
            }
        } catch (SQLException e) {
            throw new CheckRunnerException("INTERNAL SERVER ERROR");
        }
        return products;
    }
}
