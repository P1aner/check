package ru.clevertec.check.model;

import java.util.List;

public class Order {
    private List<OrderItem> orderItems;
    private DiscountCard discountCard;

    public Order(List<OrderItem> orderItems, DiscountCard discountCard) {
        this.orderItems = orderItems;
        this.discountCard = discountCard;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public DiscountCard getDiscountCard() {
        return discountCard;
    }

    public void setDiscountCard(DiscountCard discountCard) {
        this.discountCard = discountCard;
    }
}