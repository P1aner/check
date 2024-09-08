package ru.clevertec.check.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.OrderItem;
import ru.clevertec.check.services.api.CheckService;
import ru.clevertec.check.services.api.OrderItemService;
import ru.clevertec.check.services.api.OrderService;
import ru.clevertec.check.services.data.OrderTestData;

import java.math.BigDecimal;
import java.math.RoundingMode;

class CheckServiceBaseTest {
    private static final String EXPECTED_CHECK_WITHOUT_DATE_WITH_DISCOUND_CARD = "\n\nQTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL\n10;1;1.10$;1.10$;11.00$\n\nDISCOUNT CARD;DISCOUNT PERCENTAGE\n1;3%\n\nTOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT\n11.00$;1.10$;9.90$";
    private static final String EXPECTED_CHECK_WITHOUT_DATE_WITHOUT_DISCOUND_CARD = "\n\nQTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL\n10;1;1.10$;1.10$;11.00$\n\nTOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT\n11.00$;1.10$;9.90$";

    private final OrderItemService orderItemService = Mockito.mock(OrderItemServiceBase.class);
    private final OrderService orderService = Mockito.mock(OrderServiceBase.class);
    private final CheckService checkService = new CheckServiceBase(orderService, orderItemService);

    @Test
    void getCheckWithDiscountCard() {
        Order order = OrderTestData.getSimpleOrder();

        OrderItem orderItem1 = order.getOrderItems().getFirst();

        Mockito.when(orderItemService.calculateOrderItemTotalPrice(orderItem1)).thenReturn(BigDecimal.valueOf(11.00).setScale(2, RoundingMode.HALF_UP));
        Mockito.when(orderItemService.calculateOrderItemDiscount(orderItem1, order)).thenReturn(BigDecimal.valueOf(1.10).setScale(2, RoundingMode.HALF_UP));
        Mockito.when(orderService.calculateOrderTotalPrice(order)).thenReturn(BigDecimal.valueOf(11.00).setScale(2, RoundingMode.HALF_UP));
        Mockito.when(orderService.calculateOrderTotalDiscount(order)).thenReturn(BigDecimal.valueOf(1.10).setScale(2, RoundingMode.HALF_UP));
        Mockito.when(orderService.calculateOrderTotalWithDiscount(order)).thenReturn(BigDecimal.valueOf(9.90).setScale(2, RoundingMode.HALF_UP));


        String check = checkService.getCheck(order);
        String checkWithoutDate = check.substring(29);

        Assertions.assertEquals(EXPECTED_CHECK_WITHOUT_DATE_WITH_DISCOUND_CARD, checkWithoutDate);
    }

    @Test
    void getCheckWithoutDiscountCard() {
        Order order = OrderTestData.getSimpleOrderWithoutDiscountCard();

        OrderItem orderItem1 = order.getOrderItems().getFirst();

        Mockito.when(orderItemService.calculateOrderItemTotalPrice(orderItem1)).thenReturn(BigDecimal.valueOf(11.00).setScale(2, RoundingMode.HALF_UP));
        Mockito.when(orderItemService.calculateOrderItemDiscount(orderItem1, order)).thenReturn(BigDecimal.valueOf(1.10).setScale(2, RoundingMode.HALF_UP));
        Mockito.when(orderService.calculateOrderTotalPrice(order)).thenReturn(BigDecimal.valueOf(11.00).setScale(2, RoundingMode.HALF_UP));
        Mockito.when(orderService.calculateOrderTotalDiscount(order)).thenReturn(BigDecimal.valueOf(1.10).setScale(2, RoundingMode.HALF_UP));
        Mockito.when(orderService.calculateOrderTotalWithDiscount(order)).thenReturn(BigDecimal.valueOf(9.90).setScale(2, RoundingMode.HALF_UP));

        String check = checkService.getCheck(order);
        String checkWithoutDate = check.substring(29);

        Assertions.assertEquals(EXPECTED_CHECK_WITHOUT_DATE_WITHOUT_DISCOUND_CARD, checkWithoutDate);
    }
}