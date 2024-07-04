package ru.clevertec.check.utils;

import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Product;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MapperFromResultSet {

    public static final String ID = "id";
    public static final String NUMBER = "number";
    public static final String AMOUNT = "amount";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "price";
    public static final String QUANTITY_IN_STOCK = "quantity_in_stock";
    public static final String WHOLESALE_PRODUCT = "wholesale_product";

    private MapperFromResultSet() {
    }

    public static DiscountCard discountCardMapper(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong(ID);
        int number = resultSet.getInt(NUMBER);
        short amount = resultSet.getShort(AMOUNT);
        return new DiscountCard(id, number, amount);
    }

    public static Product productMapper(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong(ID);
        String description = resultSet.getString(DESCRIPTION);
        BigDecimal price = resultSet.getBigDecimal(PRICE);
        Integer quantityInStock = resultSet.getInt(QUANTITY_IN_STOCK);
        boolean wholesaleProduct = resultSet.getBoolean(WHOLESALE_PRODUCT);
        return new Product(id, description, price, quantityInStock, wholesaleProduct);
    }
}
