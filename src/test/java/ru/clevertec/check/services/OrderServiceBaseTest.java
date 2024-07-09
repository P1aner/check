package ru.clevertec.check.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.OrderItem;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.repository.api.DiscountCardRepository;
import ru.clevertec.check.repository.api.ProductRepository;
import ru.clevertec.check.services.api.OrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.postgresql.hostchooser.HostRequirement.any;
import static ru.clevertec.check.config.Constants.WHOLESALE_COUNT;

class OrderServiceBaseTest {

    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final DiscountCardRepository discountCardRepository = Mockito.mock(DiscountCardRepository.class);
    private final OrderService orderService = new OrderServiceBase(new OrderItemServiceBase(), productRepository, discountCardRepository);

    @Test
    void calculateOrderTotalPrice() {
        Order order = getOrder(WHOLESALE_COUNT + 1);
        BigDecimal bigDecimal = orderService.calculateOrderTotalPrice(order);
        Assertions.assertEquals(0, bigDecimal.compareTo(BigDecimal.valueOf(13.80)));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1, 1.38",
            "-1, 0.19",
    })
    void calculateOrderTotalDiscountWithWholesale(Integer shift, BigDecimal price) {
        Order order = getOrder(WHOLESALE_COUNT + shift);
        BigDecimal bigDecimal = orderService.calculateOrderTotalDiscount(order);
        Assertions.assertEquals(0, bigDecimal.compareTo(price));
    }

    @Test
    void calculateOrderTotalWithDiscount() {
        Order order = getOrder(WHOLESALE_COUNT - 1);
        BigDecimal bigDecimal = orderService.calculateOrderTotalWithDiscount(order);
        Assertions.assertEquals(0, bigDecimal.compareTo(BigDecimal.valueOf(9.01)));
    }

    @Test
    void isEnoughMoneyTrue() {
        Order order = getOrder(WHOLESALE_COUNT);
        boolean enoughMoney = orderService.isEnoughMoney(order, BigDecimal.valueOf(10.36));
        System.out.println(orderService.calculateOrderTotalWithDiscount(order));
        Assertions.assertEquals(true, enoughMoney);
    }

    @Test
    void isEnoughMoneyFalse() {
        Order order = getOrder(WHOLESALE_COUNT);
        CheckRunnerException thrown = Assertions.assertThrows(CheckRunnerException.class, () -> {
            orderService.isEnoughMoney(order, BigDecimal.valueOf(10.34));
        });
        Assertions.assertEquals("NOT ENOUGH MONEY", thrown.getMessage());
    }

    @Test
    void createOrderNegativeCaseNotEnoughQuantityInStock() {
        Map<Long, Integer> integerIntegerMap = Map.of(1L, 2);
        Product product = new Product(1L, "1", BigDecimal.valueOf(1.1), 1, true);
        Set<Long> longs = integerIntegerMap.keySet();
        Mockito.when(productRepository.findByIds(longs)).thenReturn(List.of(product));
        Mockito.when(discountCardRepository.findByNumber(any.ordinal())).thenReturn(Optional.empty());
        CheckRunnerException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
            orderService.createOrder(integerIntegerMap, "1111");
        });
        Assertions.assertEquals(BadRequestException.class.getCanonicalName(), thrown.getClass().getCanonicalName());
    }

    @Test
    void createOrderNegativeCaseInOrderedProducts() {
        Map<Long, Integer> integerIntegerMap = Map.of(1L, 2, 2L, 1);
        Product product = new Product(1L, "1", BigDecimal.valueOf(1.1), 1, true);
        Set<Long> longs = integerIntegerMap.keySet();
        Mockito.when(productRepository.findByIds(longs)).thenReturn(List.of(product));
        CheckRunnerException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
            orderService.createOrder(integerIntegerMap, "1111");
        });
        Assertions.assertEquals(BadRequestException.class.getCanonicalName(), thrown.getClass().getCanonicalName());
    }

    @Test
    void createOrderPositiveCase() {
        Map<Long, Integer> integerIntegerMap = Map.of(1L, 2);
        Product product = new Product(1L, "1", BigDecimal.valueOf(1.1), 3, true);
        Set<Long> longs = integerIntegerMap.keySet();
        DiscountCard discountCard = new DiscountCard(1111, (short) 3);
        Mockito.when(productRepository.findByIds(longs)).thenReturn(List.of(product));
        Mockito.when(discountCardRepository.findByNumber(1111)).thenReturn(Optional.of(discountCard));
        Order order = orderService.createOrder(integerIntegerMap, "1111");
        boolean equals = order.getOrderItems().getFirst().getProduct().equals(product);
        boolean equals1 = order.getDiscountCard().equals(discountCard);
        Assertions.assertTrue(equals);
        Assertions.assertTrue(equals1);
    }

    @Test
    void completeOrderNegativeCaseNotEnoughProducts() {
        Product product = new Product(1L, "1", BigDecimal.valueOf(1.1), 3, true);
        DiscountCard discountCard = new DiscountCard(1111, (short) 3);
        Mockito.when(productRepository.findByIds(any())).thenReturn(List.of(product));
        Order order = new Order(List.of(new OrderItem(product, 10)), discountCard);
        CheckRunnerException thrown = Assertions.assertThrows(CheckRunnerException.class, () -> {
            orderService.completeOrder(order, BigDecimal.valueOf(10));
        });
        Assertions.assertEquals(CheckRunnerException.class, thrown.getClass());
    }

    @Test
    void completeOrderNegativeCaseCountFromDb() {
        Product product1 = new Product(1L, "1", BigDecimal.valueOf(1.1), 11, true);
        Product product2 = new Product(2L, "1", BigDecimal.valueOf(1.1), 3, true);
        DiscountCard discountCard = new DiscountCard(1111, (short) 3);
        Mockito.when(productRepository.findByIds(any())).thenReturn(List.of(product1));
        Order order = new Order(List.of(new OrderItem(product1, 10), new OrderItem(product2, 10)), discountCard);
        CheckRunnerException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
            orderService.completeOrder(order, BigDecimal.valueOf(100));
        });
        Assertions.assertEquals(BadRequestException.class, thrown.getClass());
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