package ru.clevertec.check.repository;

import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.repository.api.DiscountCardRepository;
import ru.clevertec.check.utils.JDBCConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static ru.clevertec.check.utils.MapperFromResultSet.discountCardMapper;

public class DiscountCardRepositorySqL implements DiscountCardRepository {
    public static final String SELECT_FROM_DISCOUNT_CARD_WHERE_NUMBER = "SELECT * FROM discount_card WHERE number = ?";
    public static final String SELECT_FROM_DISCOUNT_CARD_WHERE_ID = "SELECT * FROM discount_card WHERE id = ?";
    public static final String INSERT_INTO_PUBLIC_DISCOUNT_CARD_NUMBER_AMOUNT_VALUES = "INSERT INTO discount_card (number, amount) VALUES (?, ?)";
    public static final String UPDATE_PUBLIC_DISCOUNT_CARD_SET_NUMBER_AMOUNT_WHERE_ID = "UPDATE discount_card SET number = ?, amount = ? WHERE id = ?";
    public static final String DELETE_FROM_DISCOUNT_CARD_WHERE_ID_VALUES = "DELETE FROM discount_card WHERE id = ?";
    public static final String SELECT_EXISTS_SELECT_FROM_DISCOUNT_CARD_WHERE_ID = "SELECT EXISTS (SELECT * FROM discount_card WHERE id = ?)";
    private final JDBCConnector connector = JDBCConnector.getInstance();

    @Override
    public Long save(DiscountCard discountCard) {
        Long id = null;
        Connection connection = connector.getConnection();
        if (discountCard.getId() == null) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_PUBLIC_DISCOUNT_CARD_NUMBER_AMOUNT_VALUES, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, discountCard.getNumber());
                preparedStatement.setShort(2, discountCard.getAmount());
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                while (resultSet.next()) {
                    id = resultSet.getLong(1);
                }
            } catch (SQLException e) {
                throw CheckRunnerException.internalServerError();
            }
        } else {
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PUBLIC_DISCOUNT_CARD_SET_NUMBER_AMOUNT_WHERE_ID)) {
                preparedStatement.setInt(1, discountCard.getNumber());
                preparedStatement.setShort(2, discountCard.getAmount());
                preparedStatement.setLong(3, discountCard.getId());
                preparedStatement.executeUpdate();
                id = discountCard.getId();
            } catch (SQLException e) {
                throw CheckRunnerException.internalServerError();
            }
        }
        return id;
    }

    @Override
    public Optional<DiscountCard> findByNumber(Integer number) {
        DiscountCard discountCard = null;
        Connection connection = connector.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_DISCOUNT_CARD_WHERE_NUMBER)) {
            preparedStatement.setInt(1, number);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                discountCard = discountCardMapper(resultSet);
            }
        } catch (SQLException e) {
            throw CheckRunnerException.internalServerError();
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
            throw CheckRunnerException.internalServerError();
        }
        return Optional.ofNullable(discountCard);
    }

    @Override
    public void deleteById(long id) {
        Connection connection = connector.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FROM_DISCOUNT_CARD_WHERE_ID_VALUES)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw CheckRunnerException.internalServerError();
        }
    }

    @Override
    public boolean exists(long id) {
        Connection connection = connector.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EXISTS_SELECT_FROM_DISCOUNT_CARD_WHERE_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getBoolean(1);
        } catch (SQLException e) {
            throw CheckRunnerException.internalServerError();
        }
    }
}