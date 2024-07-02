package ru.clevertec.check.services;

import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.OrderItem;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.services.api.OrderItemService;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static ru.clevertec.check.config.AppConfig.WHOLESALE_COUNT;
import static ru.clevertec.check.config.AppConfig.WHOLESALE_PERCENT;

public class OrderItemServiceImpl implements OrderItemService {

    public BigDecimal calculateOrderItemTotalPrice(OrderItem orderItem) {
        return orderItem.getProduct().getPrice()
                .multiply(new BigDecimal(orderItem.getCount()))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateOrderItemDiscount(OrderItem orderItem, Order order) {
        return calculateOrderItemTotalPrice(orderItem).multiply(BigDecimal.valueOf(getDiscount(orderItem, order) / 100.0))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private Short getDiscount(OrderItem orderItem, Order order) {
        Product product = orderItem.getProduct();
        if (orderItem.getCount() >= WHOLESALE_COUNT && product.getWholesaleProduct()) {
            return WHOLESALE_PERCENT;
        } else if (order.getDiscountCard() != null) {
            return order.getDiscountCard().getAmount();
        } else {
            return 0;
        }
    }
}