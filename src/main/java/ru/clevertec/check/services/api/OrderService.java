package ru.clevertec.check.services.api;

import ru.clevertec.check.model.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OrderService {
    Order createOrder(Map<Long, Integer> productsArgs, String discountCardId);

    BigDecimal calculateOrderTotalPrice(Order order);

    BigDecimal calculateOrderTotalDiscount(Order order);

    BigDecimal calculateOrderTotalWithDiscount(Order order);

    boolean isEnoughMoney(Order order, BigDecimal money);
}
