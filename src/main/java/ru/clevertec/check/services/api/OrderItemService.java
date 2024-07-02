package ru.clevertec.check.services.api;

import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.OrderItem;

import java.math.BigDecimal;

public interface OrderItemService {
    BigDecimal calculateOrderItemTotalPrice(OrderItem orderItem);

    BigDecimal calculateOrderItemDiscount(OrderItem orderItem, Order order);
}
