package ru.clevertec.check.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.OrderItem;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.services.api.CheckService;
import ru.clevertec.check.services.api.OrderItemService;
import ru.clevertec.check.services.api.OrderService;

import java.math.BigDecimal;
import java.util.List;

class CheckServiceBaseTest {
    private final OrderItemService orderItemService = new OrderItemServiceBase();
    private final OrderService orderService = new OrderServiceBase(orderItemService, null, null);
    private final CheckService checkService = new CheckServiceBase(orderService, orderItemService);

    @Test
    void getCheckWithDiscountCard() {
        DiscountCard discountCard = new DiscountCard(1L, 1, (short) 2);
        Product product = new Product(1L, "1", BigDecimal.valueOf(1.1), 10, true);
        OrderItem orderItem = new OrderItem(product, 3);
        Order order = new Order(List.of(orderItem), discountCard);
        String check = checkService.getCheck(order);
        String actualCheckWithoutDate = "\n\nQTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL\n3;1;1.1$;0.07$;3.30$\n\nDISCOUNT CARD;DISCOUNT PERCENTAGE\n1;2%\n\nTOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT\n3.30$;0.07$;3.23$";
        String checkWithoutDate = check.substring(29);
        Assertions.assertEquals(checkWithoutDate, actualCheckWithoutDate);
    }

    @Test
    void getCheckWithoutDiscountCard() {
        Product product = new Product(1L, "1", BigDecimal.valueOf(1.1), 10, true);
        OrderItem orderItem = new OrderItem(product, 3);
        Order order = new Order(List.of(orderItem), null);
        String check = checkService.getCheck(order);
        String actualCheckWithoutDate = "\n\nQTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL\n3;1;1.1$;0.00$;3.30$\n\nTOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT\n3.30$;0.00$;3.30$";
        String checkWithoutDate = check.substring(29);
        Assertions.assertEquals(checkWithoutDate, actualCheckWithoutDate);
    }
}