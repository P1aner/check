package ru.clevertec.check.repository;

import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.repository.api.ProductRepository;
import ru.clevertec.check.utils.JDBCConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static ru.clevertec.check.utils.MapperFromResultSet.productMapper;

public class ProductRepositorySQL implements ProductRepository {
    public static final String SELECT_FROM_PRODUCT_WHERE_ID = "SELECT * FROM product WHERE id = ?";
    private final JDBCConnector connector = JDBCConnector.getInstance();

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
}
