package ru.clevertec.check.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.OrderItem;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.services.api.OrderService;

import java.math.BigDecimal;
import java.util.List;

import static ru.clevertec.check.config.AppConfig.WHOLESALE_COUNT;

class OrderServiceBaseTest {

    @Test
    void calculateOrderTotalPrice() {
        Order order = getOrder(WHOLESALE_COUNT + 1);
        OrderService orderService = new OrderServiceBase();
        BigDecimal bigDecimal = orderService.calculateOrderTotalPrice(order);
        Assertions.assertEquals(0, bigDecimal.compareTo(BigDecimal.valueOf(13.80)));
    }

    @Test
    void calculateOrderTotalDiscountWithWholesale() {
        Order order = getOrder(WHOLESALE_COUNT + 1);
        OrderService orderService = new OrderServiceBase();
        BigDecimal bigDecimal = orderService.calculateOrderTotalDiscount(order);
        Assertions.assertEquals(0, bigDecimal.compareTo(BigDecimal.valueOf(1.38)));
    }

    @Test
    void calculateOrderTotalDiscountWithoutWholesale() {
        Order order = getOrder(WHOLESALE_COUNT - 1);
        OrderService orderService = new OrderServiceBase();
        BigDecimal bigDecimal = orderService.calculateOrderTotalDiscount(order);
        Assertions.assertEquals(0, bigDecimal.compareTo(BigDecimal.valueOf(0.19)));
    }

    @Test
    void calculateOrderTotalWithDiscount() {
        Order order = getOrder(WHOLESALE_COUNT - 1);
        OrderService orderService = new OrderServiceBase();
        BigDecimal bigDecimal = orderService.calculateOrderTotalWithDiscount(order);
        Assertions.assertEquals(0, bigDecimal.compareTo(BigDecimal.valueOf(9.01)));
    }

    @Test
    void isEnoughMoneyTrue() {
        OrderService orderService = new OrderServiceBase();
        Order order = getOrder(WHOLESALE_COUNT);
        boolean enoughMoney = orderService.isEnoughMoney(order, BigDecimal.valueOf(10.36));
        System.out.println(orderService.calculateOrderTotalWithDiscount(order));
        Assertions.assertEquals(true, enoughMoney);
    }

    @Test
    void isEnoughMoneyFalse() {
        OrderService orderService = new OrderServiceBase();
        Order order = getOrder(WHOLESALE_COUNT);
        CheckRunnerException thrown = Assertions.assertThrows(CheckRunnerException.class, () -> {
            orderService.isEnoughMoney(order, BigDecimal.valueOf(10.34));
        });
        Assertions.assertEquals("NOT ENOUGH MONEY", thrown.getMessage());
    }

    private static Order getOrder(int WHOLESALE_COUNT) {
        DiscountCard discountCard = new DiscountCard(1L, 1, (short) 2);
        Product product1 = new Product(1L, "1", BigDecimal.valueOf(1.1), 10, true);
        Product product2 = new Product(1L, "1", BigDecimal.valueOf(1.2), 10, true);
        OrderItem orderItem1 = new OrderItem(product1, WHOLESALE_COUNT);
        OrderItem orderItem2 = new OrderItem(product2, WHOLESALE_COUNT);
        return new Order(List.of(orderItem1, orderItem2), discountCard);
    }
}