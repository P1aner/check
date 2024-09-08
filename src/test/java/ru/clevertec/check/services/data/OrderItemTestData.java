package ru.clevertec.check.services.data;

import ru.clevertec.check.model.OrderItem;
import ru.clevertec.check.model.Product;

public class OrderItemTestData {

    public static OrderItem getOrderItem1(int wholesaleCount) {
        Product product1 = ProductTestData.getProductWithIdAndQuantityMore1();
        return new OrderItem(product1, wholesaleCount);
    }

    public static OrderItem getOrderItem2(int wholesaleCount) {
        Product product2 = ProductTestData.getProductWithIdAndQuantityMore2();
        return new OrderItem(product2, wholesaleCount);
    }

}
