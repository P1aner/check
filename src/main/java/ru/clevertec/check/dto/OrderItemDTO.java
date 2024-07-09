package ru.clevertec.check.dto;

public class OrderItemDTO {
    private long id;
    private int quantity;

    public OrderItemDTO() {
    }

    public OrderItemDTO(int id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", quantity=" + quantity +
                '}';
    }
}