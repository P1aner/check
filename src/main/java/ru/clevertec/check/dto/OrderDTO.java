package ru.clevertec.check.dto;

import java.math.BigDecimal;
import java.util.ArrayList;

public class OrderDTO {
    private ArrayList<OrderItemDTO> orderItemDTOS;
    private int discountCard;
    private BigDecimal balanceDebitCard;

    public OrderDTO() {
    }

    public OrderDTO(ArrayList<OrderItemDTO> orderItemDTOS, int discountCard, BigDecimal balanceDebitCard) {
        this.orderItemDTOS = orderItemDTOS;
        this.discountCard = discountCard;
        this.balanceDebitCard = balanceDebitCard;
    }

    public ArrayList<OrderItemDTO> getProducts() {
        return orderItemDTOS;
    }

    public void setProducts(ArrayList<OrderItemDTO> orderItemDTOS) {
        this.orderItemDTOS = orderItemDTOS;
    }

    public int getDiscountCard() {
        return discountCard;
    }

    public void setDiscountCard(int discountCard) {
        this.discountCard = discountCard;
    }

    public BigDecimal getBalanceDebitCard() {
        return balanceDebitCard;
    }

    public void setBalanceDebitCard(BigDecimal balanceDebitCard) {
        this.balanceDebitCard = balanceDebitCard;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "products=" + orderItemDTOS +
                ", discountCard=" + discountCard +
                ", balanceDebitCard=" + balanceDebitCard +
                '}';
    }
}