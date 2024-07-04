package ru.clevertec.check.services;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.OrderItem;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.services.api.OrderItemService;

import java.math.BigDecimal;
import java.util.List;

import static ru.clevertec.check.config.AppConfig.WHOLESALE_COUNT;

class OrderItemServiceBaseTest {

    @Test
    void calculateOrderItemTotalPriceWithoutRounding() {
        BigDecimal bigDecimal = getBigDecimal(1.10);
        Assertions.assertEquals(0, bigDecimal.compareTo(BigDecimal.valueOf(2.20)));
    }

    @Test
    void calculateOrderItemTotalPriceWithRoundingUp() {
        BigDecimal bigDecimal = getBigDecimal(1.111);
        Assertions.assertEquals(0, bigDecimal.compareTo(BigDecimal.valueOf(2.22)));
    }

    @Test
    void calculateOrderItemTotalPriceWithRoundingUp1() {
        BigDecimal bigDecimal = getBigDecimal(1.119);
        Assertions.assertEquals(0, bigDecimal.compareTo(BigDecimal.valueOf(2.24)));
    }


    @Test
    void calculateOrderItemDiscountWithoutWholesale() {
        BigDecimal bigDecimal = getBigDecimal(WHOLESALE_COUNT - 1, 1.11);
        Assertions.assertEquals(0, bigDecimal.compareTo(BigDecimal.valueOf(0.09)));
    }

    @Test
    void calculateOrderItemDiscountWithWholesale() {
        BigDecimal bigDecimal = getBigDecimal(WHOLESALE_COUNT + 1, 1.11);
        Assertions.assertEquals(0, bigDecimal.compareTo(BigDecimal.valueOf(0.67)));
    }

    private static BigDecimal getBigDecimal(int WHOLESALE_COUNT, double price) {
        DiscountCard discountCard = new DiscountCard(1L, 1, (short) 2);
        Product product = new Product(1L, "1", BigDecimal.valueOf(price), 10, true);
        OrderItem orderItem = new OrderItem(product, WHOLESALE_COUNT);
        Order order = new Order(List.of(orderItem), discountCard);
        OrderItemService orderItemService = new OrderItemServiceBase();
        return orderItemService.calculateOrderItemDiscount(orderItem, order);
    }

    private static BigDecimal getBigDecimal(double val) {
        OrderItemService orderItemService = new OrderItemServiceBase();
        Product product = new Product(1L, "1", BigDecimal.valueOf(val), 10, true);
        OrderItem orderItem = new OrderItem(product, 2);
        return orderItemService.calculateOrderItemTotalPrice(orderItem);
    }
}