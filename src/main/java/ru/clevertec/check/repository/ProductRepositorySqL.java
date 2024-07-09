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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ru.clevertec.check.utils.MapperFromResultSet.productMapper;

public class ProductRepositorySqL implements ProductRepository {
    public static final String SELECT_FROM_PRODUCT_WHERE_ID = "SELECT * FROM product WHERE id = ?";
    public static final String INSERT_INTO_PRODUCT_DESCRIPTION_PRICE_QUANTITY_IN_STOCK_WHOLESALE_PRODUCT_VALUES = "INSERT INTO product (description, price, quantity_in_stock, wholesale_product) VALUES (?, ?, ?, ?)";
    public static final String UPDATE_PRODUCT_SET_DESCRIPTION_PRICE_QUANTITY_IN_STOCK_WHOLESALE_PRODUCT_WHERE_ID = "UPDATE product SET description = ?, price = ?, quantity_in_stock = ?, wholesale_product = ? WHERE id = ?";
    public static final String DELETE_FROM_PRODUCT_WHERE_ID_VALUES = "DELETE FROM product WHERE id = ?";
    public static final String SELECT_FROM_PRODUCT_WHERE_ID_ANY = "SELECT * FROM product WHERE id = any (?)";
    public static final String SELECT_EXISTS_SELECT_FROM_PRODUCT_WHERE_ID = "SELECT EXISTS (SELECT * FROM product WHERE id = ?)";
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
    public Long save(Product product) {
        Connection connection = connector.getConnection();
        Long id = null;
        if (product.getId() == null) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_PRODUCT_DESCRIPTION_PRICE_QUANTITY_IN_STOCK_WHOLESALE_PRODUCT_VALUES, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, product.getDescription());
                preparedStatement.setBigDecimal(2, product.getPrice());
                preparedStatement.setInt(3, product.getQuantityInStock());
                preparedStatement.setBoolean(4, product.isWholesaleProduct());
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                while (resultSet.next()) {
                    id = resultSet.getLong(1);
                }
            } catch (SQLException e) {
                throw CheckRunnerException.internalServerError();
            }
        } else {
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT_SET_DESCRIPTION_PRICE_QUANTITY_IN_STOCK_WHOLESALE_PRODUCT_WHERE_ID)) {
                preparedStatement.setString(1, product.getDescription());
                preparedStatement.setBigDecimal(2, product.getPrice());
                preparedStatement.setInt(3, product.getQuantityInStock());
                preparedStatement.setBoolean(4, product.isWholesaleProduct());
                preparedStatement.setLong(5, product.getId());
                preparedStatement.executeUpdate();
                id = product.getId();
            } catch (SQLException e) {
                throw CheckRunnerException.internalServerError();
            }
        }
        return id;
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
            throw CheckRunnerException.internalServerError();
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
            throw CheckRunnerException.internalServerError();
        }
        return products;
    }

    @Override
    public void updateAll(List<Product> products) {
        Connection connection = connector.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT_SET_DESCRIPTION_PRICE_QUANTITY_IN_STOCK_WHOLESALE_PRODUCT_WHERE_ID)) {
            for (Product product : products) {
                preparedStatement.setString(1, product.getDescription());
                preparedStatement.setBigDecimal(2, product.getPrice());
                preparedStatement.setInt(3, product.getQuantityInStock());
                preparedStatement.setBoolean(4, product.isWholesaleProduct());
                preparedStatement.setLong(5, product.getId());
                preparedStatement.addBatch();
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw CheckRunnerException.internalServerError();
        }
    }

    @Override
    public void deleteById(long id) {
        Connection connection = connector.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FROM_PRODUCT_WHERE_ID_VALUES)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw CheckRunnerException.internalServerError();
        }
    }

    @Override
    public boolean exists(long id) {
        Connection connection = connector.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EXISTS_SELECT_FROM_PRODUCT_WHERE_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getBoolean(1);
        } catch (SQLException e) {
            throw CheckRunnerException.internalServerError();
        }
    }
}