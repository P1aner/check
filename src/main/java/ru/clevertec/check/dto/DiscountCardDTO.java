package ru.clevertec.check.dto;

public class DiscountCardDTO {
    private int discountCard;
    private short discountAmount;

    public DiscountCardDTO() {
    }

    public DiscountCardDTO(int discountCard, short discountAmount) {
        this.discountCard = discountCard;
        this.discountAmount = discountAmount;
    }

    public int getDiscountCard() {
        return discountCard;
    }

    public void setDiscountCard(int discountCard) {
        this.discountCard = discountCard;
    }

    public short getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(short discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public String toString() {
        return "DiscountCardDTO{" +
                "discountCard=" + discountCard +
                ", discountAmount=" + discountAmount +
                '}';
    }
}