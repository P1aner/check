package ru.clevertec.check.services;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.OrderItem;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.services.api.OrderItemService;

import java.math.BigDecimal;
import java.util.List;

import static ru.clevertec.check.config.Constants.WHOLESALE_COUNT;


class OrderItemServiceBaseTest {

    @ParameterizedTest
    @CsvSource(value = {
            "2.20, 1.10",
            "2.22, 1.111",
            "2.24, 1.119"
    })
    void calculateOrderItemTotalPrice(BigDecimal expectedOrderItemTotalPrice, BigDecimal productPrice) {
        OrderItemService orderItemService = new OrderItemServiceBase();
        Product product = new Product(1L, "1", productPrice, 10, true);
        OrderItem orderItem = new OrderItem(product, 2);
        BigDecimal calculatedOrderItemTotalPrice = orderItemService.calculateOrderItemTotalPrice(orderItem);
        Assertions.assertEquals(0, calculatedOrderItemTotalPrice.compareTo(expectedOrderItemTotalPrice));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1.11, -1, 0.09",
            "1.11, 1, 0.67"
    })
    void calculateOrderItemDiscount(BigDecimal price, Integer shift, BigDecimal calculatePrice) {
        DiscountCard discountCard = new DiscountCard(1L, 1, (short) 2);
        Product product = new Product(1L, "1", price, 10, true);
        OrderItem orderItem = new OrderItem(product, WHOLESALE_COUNT + shift);
        Order order = new Order(List.of(orderItem), discountCard);
        OrderItemService orderItemService = new OrderItemServiceBase();
        BigDecimal bigDecimal = orderItemService.calculateOrderItemDiscount(orderItem, order);
        Assertions.assertEquals(0, bigDecimal.compareTo(calculatePrice));
    }
}