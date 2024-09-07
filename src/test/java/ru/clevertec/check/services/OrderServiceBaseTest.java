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
import ru.clevertec.check.services.api.OrderItemService;
import ru.clevertec.check.services.api.OrderService;
import ru.clevertec.check.services.data.DiscountCardTestData;
import ru.clevertec.check.services.data.OrderTestData;
import ru.clevertec.check.services.data.ProductTestData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.postgresql.hostchooser.HostRequirement.any;
import static ru.clevertec.check.config.Constants.WHOLESALE_COUNT;

class OrderServiceBaseTest {

    private static final String NOT_ENOUGH_MONEY = "NOT ENOUGH MONEY";
    public static final String DISCOUNT_CARD_ID = "1111";

    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final DiscountCardRepository discountCardRepository = Mockito.mock(DiscountCardRepository.class);
    private final OrderItemService orderItemServiceBase = Mockito.mock(OrderItemServiceBase.class);
    private final OrderService orderService = new OrderServiceBase(orderItemServiceBase, productRepository, discountCardRepository);

    @Test
    void calculateOrderTotalPrice() {
        Order order = OrderTestData.getOrder(WHOLESALE_COUNT + 1);
        OrderItem orderItem1 = order.getOrderItems().get(0);
        OrderItem orderItem2 = order.getOrderItems().get(1);

        Mockito.when(orderItemServiceBase.calculateOrderItemTotalPrice(orderItem1)).thenReturn(BigDecimal.valueOf(6.60));
        Mockito.when(orderItemServiceBase.calculateOrderItemTotalPrice(orderItem2)).thenReturn(BigDecimal.valueOf(7.20));

        BigDecimal bigDecimal = orderService.calculateOrderTotalPrice(order);

        Assertions.assertEquals(0, bigDecimal.compareTo(BigDecimal.valueOf(13.80)));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1, 1.38",
            "-1, 0.19"
    })
    void calculateOrderTotalDiscountWithWholesale(Integer shift, BigDecimal price) {
        Order order = OrderTestData.getOrder(WHOLESALE_COUNT + shift);
        OrderItem orderItem1 = order.getOrderItems().get(0);
        OrderItem orderItem2 = order.getOrderItems().get(1);
        if (shift == 1) {
            Mockito.when(orderItemServiceBase.calculateOrderItemDiscount(orderItem1, order)).thenReturn(BigDecimal.valueOf(0.66));
            Mockito.when(orderItemServiceBase.calculateOrderItemDiscount(orderItem2, order)).thenReturn(BigDecimal.valueOf(0.72));
        } else {
            Mockito.when(orderItemServiceBase.calculateOrderItemDiscount(orderItem1, order)).thenReturn(BigDecimal.valueOf(0.09));
            Mockito.when(orderItemServiceBase.calculateOrderItemDiscount(orderItem2, order)).thenReturn(BigDecimal.valueOf(0.1));
        }

        BigDecimal bigDecimal = orderService.calculateOrderTotalDiscount(order);

        Assertions.assertEquals(0, bigDecimal.compareTo(price));
    }

    @Test
    void calculateOrderTotalWithDiscount() {
        Order order = OrderTestData.getOrder(WHOLESALE_COUNT - 1);
        OrderItem orderItem1 = order.getOrderItems().get(0);
        OrderItem orderItem2 = order.getOrderItems().get(1);

        Mockito.when(orderItemServiceBase.calculateOrderItemTotalPrice(orderItem1)).thenReturn(BigDecimal.valueOf(4.40));
        Mockito.when(orderItemServiceBase.calculateOrderItemTotalPrice(orderItem2)).thenReturn(BigDecimal.valueOf(4.80));
        Mockito.when(orderItemServiceBase.calculateOrderItemDiscount(orderItem1, order)).thenReturn(BigDecimal.valueOf(0.09));
        Mockito.when(orderItemServiceBase.calculateOrderItemDiscount(orderItem2, order)).thenReturn(BigDecimal.valueOf(0.10));

        BigDecimal bigDecimal = orderService.calculateOrderTotalWithDiscount(order);

        Assertions.assertEquals(0, bigDecimal.compareTo(BigDecimal.valueOf(9.01)));
    }

    @Test
    void isEnoughMoneyTrue() {
        Order order = OrderTestData.getOrder(WHOLESALE_COUNT);
        OrderItem orderItem1 = order.getOrderItems().get(0);
        OrderItem orderItem2 = order.getOrderItems().get(1);

        Mockito.when(orderItemServiceBase.calculateOrderItemTotalPrice(orderItem1)).thenReturn(BigDecimal.valueOf(5.50));
        Mockito.when(orderItemServiceBase.calculateOrderItemTotalPrice(orderItem2)).thenReturn(BigDecimal.valueOf(6.00));
        Mockito.when(orderItemServiceBase.calculateOrderItemDiscount(orderItem1, order)).thenReturn(BigDecimal.valueOf(0.55));
        Mockito.when(orderItemServiceBase.calculateOrderItemDiscount(orderItem2, order)).thenReturn(BigDecimal.valueOf(0.60));

        boolean enoughMoney = orderService.isEnoughMoney(order, BigDecimal.valueOf(10.36));

        Assertions.assertTrue(enoughMoney);
    }

    @Test
    void isEnoughMoneyFalse() {
        Order order = OrderTestData.getOrder(WHOLESALE_COUNT);
        OrderItem orderItem1 = order.getOrderItems().get(0);
        OrderItem orderItem2 = order.getOrderItems().get(1);

        Mockito.when(orderItemServiceBase.calculateOrderItemTotalPrice(orderItem1)).thenReturn(BigDecimal.valueOf(5.50));
        Mockito.when(orderItemServiceBase.calculateOrderItemTotalPrice(orderItem2)).thenReturn(BigDecimal.valueOf(6.00));
        Mockito.when(orderItemServiceBase.calculateOrderItemDiscount(orderItem1, order)).thenReturn(BigDecimal.valueOf(0.55));
        Mockito.when(orderItemServiceBase.calculateOrderItemDiscount(orderItem2, order)).thenReturn(BigDecimal.valueOf(0.60));

        BigDecimal money = BigDecimal.valueOf(10.34);

        CheckRunnerException thrown = Assertions.assertThrows(CheckRunnerException.class, () -> orderService.isEnoughMoney(order, money));
        Assertions.assertEquals(NOT_ENOUGH_MONEY, thrown.getMessage());
    }

    @Test
    void createOrderNegativeCaseNotEnoughQuantityInStock() {
        Product product = ProductTestData.getProductWithIdAndQuantitySmall();

        Map<Long, Integer> integerIntegerMap = Map.of(1L, 5);
        Set<Long> longs = integerIntegerMap.keySet();

        Mockito.when(productRepository.findByIds(longs)).thenReturn(List.of(product));
        Mockito.when(discountCardRepository.findByNumber(any.ordinal())).thenReturn(Optional.empty());

        Assertions.assertThrows(BadRequestException.class, () -> orderService.createOrder(integerIntegerMap, DISCOUNT_CARD_ID));
    }

    @Test
    void createOrderNegativeCaseInOrderedProducts() {
        Product product = ProductTestData.getProductWithIdAndQuantitySmall();

        Map<Long, Integer> integerMap = Map.of(1L, 2, 2L, 1);
        Set<Long> longs = integerMap.keySet();

        Mockito.when(productRepository.findByIds(longs)).thenReturn(List.of(product));
        CheckRunnerException thrown = Assertions.assertThrows(BadRequestException.class, () -> orderService.createOrder(integerMap, DISCOUNT_CARD_ID));
        Assertions.assertEquals(BadRequestException.class.getCanonicalName(), thrown.getClass().getCanonicalName());
    }

    @Test
    void createOrderPositiveCase() {
        Product product = ProductTestData.getProductWithIdAndQuantitySmall();
        DiscountCard discountCard = DiscountCardTestData.getDiscountCardWithThreePercent();

        Map<Long, Integer> integerIntegerMap = Map.of(1L, 2);
        Set<Long> longs = integerIntegerMap.keySet();

        Mockito.when(productRepository.findByIds(longs)).thenReturn(List.of(product));
        Mockito.when(discountCardRepository.findByNumber(1111)).thenReturn(Optional.of(discountCard));

        Order order = orderService.createOrder(integerIntegerMap, "1111");

        Assertions.assertEquals(order.getOrderItems().getFirst().getProduct(), product);
        Assertions.assertEquals(order.getDiscountCard(), discountCard);
    }

    @Test
    void completeOrderNegativeCaseNotEnoughProducts() {
        Order order = OrderTestData.getSimpleOrder();
        OrderItem orderItem1 = order.getOrderItems().getFirst();
        Product product = orderItem1.getProduct();

        BigDecimal money = BigDecimal.valueOf(10);

        Mockito.when(orderItemServiceBase.calculateOrderItemTotalPrice(orderItem1)).thenReturn(BigDecimal.valueOf(11.00));
        Mockito.when(orderItemServiceBase.calculateOrderItemDiscount(orderItem1, order)).thenReturn(BigDecimal.valueOf(1.10));
        Mockito.when(productRepository.findByIds(any())).thenReturn(List.of(product));

        Assertions.assertThrows(CheckRunnerException.class, () -> orderService.completeOrder(order, money));
    }

    @Test
    void completeOrderNegativeCaseCountFromDb() {
        Order order = OrderTestData.getSimpleOrderWithTwoProducts();
        OrderItem orderItem1 = order.getOrderItems().get(0);
        OrderItem orderItem2 = order.getOrderItems().get(1);

        Mockito.when(orderItemServiceBase.calculateOrderItemTotalPrice(orderItem1)).thenReturn(BigDecimal.valueOf(5.50));
        Mockito.when(orderItemServiceBase.calculateOrderItemTotalPrice(orderItem2)).thenReturn(BigDecimal.valueOf(6.00));
        Mockito.when(orderItemServiceBase.calculateOrderItemDiscount(orderItem1, order)).thenReturn(BigDecimal.valueOf(0.55));
        Mockito.when(orderItemServiceBase.calculateOrderItemDiscount(orderItem2, order)).thenReturn(BigDecimal.valueOf(0.60));

        BigDecimal money = BigDecimal.valueOf(100);

        Mockito.when(productRepository.findByIds(any())).thenReturn(List.of());

        Assertions.assertThrows(BadRequestException.class, () -> orderService.completeOrder(order, money));
    }


}