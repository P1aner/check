package ru.clevertec.check.dto;

public class ProductDTO {
    private String description;
    private double price;
    private int quantity;
    private boolean isWholesale;

    public ProductDTO() {
    }

    public ProductDTO(String description, double price, int quantity, boolean isWholesale) {
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.isWholesale = isWholesale;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean getIsWholesale() {
        return isWholesale;
    }

    public void setIsWholesale(boolean wholesale) {
        isWholesale = wholesale;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", isWholesale=" + isWholesale +
                '}';
    }
}