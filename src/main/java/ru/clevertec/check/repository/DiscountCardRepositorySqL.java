package ru.clevertec.check.repository;

import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.repository.api.DiscountCardRepository;
import ru.clevertec.check.utils.JDBCConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static ru.clevertec.check.utils.MapperFromResultSet.discountCardMapper;

public class DiscountCardRepositorySqL implements DiscountCardRepository {
    public static final String SELECT_FROM_DISCOUNT_CARD_WHERE_NUMBER = "SELECT * FROM discount_card WHERE number = ?";
    public static final String SELECT_FROM_DISCOUNT_CARD_WHERE_ID = "SELECT * FROM discount_card WHERE id = ?";
    private final JDBCConnector connector = JDBCConnector.getInstance();


    @Override
    public Optional<DiscountCard> findByNumber(Integer number) {
        DiscountCard discountCard = null;
        Connection connection = connector.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_DISCOUNT_CARD_WHERE_NUMBER)) {
            preparedStatement.setLong(1, number);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                discountCard = discountCardMapper(resultSet);
            }
        } catch (SQLException e) {
            throw new CheckRunnerException("INTERNAL SERVER ERROR");
        }
        return Optional.ofNullable(discountCard);
    }

    @Override
    public Optional<DiscountCard> findById(long id) {
        DiscountCard discountCard = null;
        Connection connection = connector.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_DISCOUNT_CARD_WHERE_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                discountCard = discountCardMapper(resultSet);
            }
        } catch (SQLException e) {
            throw new CheckRunnerException("INTERNAL SERVER ERROR");
        }
        return Optional.ofNullable(discountCard);
    }
}
