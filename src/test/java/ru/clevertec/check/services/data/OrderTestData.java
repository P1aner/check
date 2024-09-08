package ru.clevertec.check.services.data;

import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.OrderItem;
import ru.clevertec.check.model.Product;

import java.util.List;

public class OrderTestData {

    public static Order getOrder(int wholesaleCount) {
        DiscountCard discountCard = DiscountCardTestData.getDiscountCardWithTwoPercent();
        OrderItem orderItem1 = OrderItemTestData.getOrderItem1(wholesaleCount);
        OrderItem orderItem2 = OrderItemTestData.getOrderItem2(wholesaleCount);
        return new Order(List.of(orderItem1, orderItem2), discountCard);
    }

    public static Order getSimpleOrder() {
        DiscountCard discountCardWithThreePercent = DiscountCardTestData.getDiscountCardWithThreePercent();
        Product productWithIdAndQuantitySmall = ProductTestData.getProductWithIdAndQuantitySmall();
        return new Order(List.of(new OrderItem(productWithIdAndQuantitySmall, 10)), discountCardWithThreePercent);
    }

    public static Order getSimpleOrderWithoutDiscountCard() {
        Product productWithIdAndQuantitySmall = ProductTestData.getProductWithIdAndQuantitySmall();
        return new Order(List.of(new OrderItem(productWithIdAndQuantitySmall, 10)), null);
    }

    public static Order getSimpleOrderWithTwoProducts() {
        Product productWithId = ProductTestData.getProductWithIdAndQuantityMore1();
        Product product2 = ProductTestData.getProductWithIdAndQuantitySmall();
        DiscountCard discountCardWithThreePercent = DiscountCardTestData.getDiscountCardWithThreePercent();
        return new Order(List.of(new OrderItem(productWithId, 10), new OrderItem(product2, 10)), discountCardWithThreePercent);
    }
}
