package ru.clevertec.check.services;

import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.OrderItem;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.services.api.OrderItemService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static ru.clevertec.check.config.AppProperties.WHOLESALE_COUNT;
import static ru.clevertec.check.config.AppProperties.WHOLESALE_PERCENT;

public class OrderItemServiceBase implements OrderItemService {

    @Override
    public BigDecimal calculateOrderItemTotalPrice(OrderItem orderItem) {
        return orderItem.getProduct().getPrice()
                .multiply(new BigDecimal(orderItem.getCount()))
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateOrderItemDiscount(OrderItem orderItem, Order order) {
        return calculateOrderItemTotalPrice(orderItem)
                .multiply(getDiscount(orderItem, order))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getDiscount(OrderItem orderItem, Order order) {
        Product product = orderItem.getProduct();
        if (orderItem.getCount() >= WHOLESALE_COUNT && product.isWholesaleProduct()) {
            return WHOLESALE_PERCENT.divide(BigDecimal.valueOf(100));
        } else {
            return Optional.ofNullable(order.getDiscountCard())
                    .map(DiscountCard::getAmount)
                    .map(BigDecimal::valueOf)
                    .map(it -> it.divide(BigDecimal.valueOf(100)))
                    .orElse(BigDecimal.ZERO);
        }
    }
}