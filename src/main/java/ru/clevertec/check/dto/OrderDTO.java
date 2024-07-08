package ru.clevertec.check.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderDTO {
    private List<OrderItemDTO> orderItemDTOS;
    private int discountCard;
    private BigDecimal balanceDebitCard;

    public OrderDTO() {
    }

    public OrderDTO(List<OrderItemDTO> orderItemDTOS, int discountCard, BigDecimal balanceDebitCard) {
        this.orderItemDTOS = orderItemDTOS;
        this.discountCard = discountCard;
        this.balanceDebitCard = balanceDebitCard;
    }

    public List<OrderItemDTO> getProducts() {
        return orderItemDTOS;
    }

    public void setProducts(List<OrderItemDTO> orderItemDTOS) {
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