package ru.clevertec.check.config;

import java.math.BigDecimal;

public class AppProperties {
    public static final String PATH_TO_PRODUCT_FILE = "./src/main/resources/products.csv";
    public static final String PATH_TO_DISCOUNT_CARD_FILE = "./src/main/resources/discountCards.csv";
    public static final String SAVE_TO_FILE = "./result.csv";
    public static final String CSV_DELIMITER = ";";
    public static final BigDecimal WHOLESALE_PERCENT = BigDecimal.valueOf(10);
    public static final Short WHOLESALE_COUNT = 5;

    private AppProperties() {
    }
}