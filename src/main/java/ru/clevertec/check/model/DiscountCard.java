package ru.clevertec.check.model;

public class DiscountCard {
    private Long id;
    private Integer number;
    private Short amount;

    public DiscountCard(Integer number, Short amount) {
        this.number = number;
        this.amount = amount;
    }

    public DiscountCard(Long id, Integer number, Short amount) {
        this.id = id;
        this.number = number;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Short getAmount() {
        return amount;
    }

    public void setAmount(Short amount) {
        this.amount = amount;
    }
}