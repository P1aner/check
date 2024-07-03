package ru.clevertec.check.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class AppConfig {
    public static String PATH_TO_PRODUCT_FILE = "./src/main/resources/products.csv";
    public static String PATH_TO_DISCOUNT_CARD_FILE = "./src/main/resources/discountCards.csv";
    public static String SAVE_TO_FILE = "./result.csv";
    public static String CSV_DELIMITER = ";";
    public static Short WHOLESALE_PERCENT = 10;
    public static Short WHOLESALE_COUNT = 5;

    private static final Logger logger = Logger.getLogger("ru.clevertec.check.CheckRunner");

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

    public static void configLogger() {
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler(SAVE_TO_FILE);
        } catch (IOException e) {
            logger.warning("INTERNAL SERVER ERROR");
            throw new RuntimeException(e);
        }
        fileHandler.setFormatter(new SimpleLoggerFormatter());
        logger.addHandler(fileHandler);
        logger.setUseParentHandlers(false);
    }
}