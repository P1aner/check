package ru.clevertec.check.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.math.BigDecimal;

public class AppConfig {
    private AppConfig() {
    }

    public static String PATH_TO_PRODUCT_FILE = "./src/main/resources/products.csv";
    public static final String PATH_TO_DISCOUNT_CARD_FILE = "./src/main/resources/discountCards.csv";
    public static String SAVE_TO_FILE = "./result.csv";
    public static final String CSV_DELIMITER = ";";
    public static final BigDecimal WHOLESALE_PERCENT = BigDecimal.valueOf(10);
    public static final Short WHOLESALE_COUNT = 5;
 

    public static void configApp(String[] args) {
        SAVE_TO_FILE = Arrays.stream(args).map(s -> s.split("="))
                .filter(strings -> strings.length == 2)
                .filter(strings -> strings[0].equals("saveToFile"))
                .map(strings -> strings[1])
                .findFirst()
                .orElseThrow(() -> {
                    logger.warning("BAD REQUEST");
                    return new RuntimeException();
                });
        PATH_TO_PRODUCT_FILE = Arrays.stream(args).map(s -> s.split("="))
                .filter(strings -> strings.length == 2)
                .filter(strings -> strings[0].equals("pathToFile"))
                .map(strings -> strings[1])
                .findFirst()
                .orElseThrow(() -> {
                    logger.warning("BAD REQUEST");
                    return new RuntimeException();
                });
    }
}