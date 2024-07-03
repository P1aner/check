package ru.clevertec.check.config;

import ru.clevertec.check.exception.CheckRunnerException;

import java.math.BigDecimal;
import java.util.Arrays;

import static ru.clevertec.check.controllers.ConsoleController.REGEX_PAIR;

public class AppConfig {
    private AppConfig() {
    }

    public static String pathToProductFile = "./src/main/resources/products.csv";
    public static String saveToFile = "./result.csv";
    public static final String PATH_TO_FILE = "pathToFile";
    public static final String SAVE_TO_FILE = "saveToFile";
    public static final String PATH_TO_DISCOUNT_CARD_FILE = "./src/main/resources/discountCards.csv";
    public static final String CSV_DELIMITER = ";";
    public static final BigDecimal WHOLESALE_PERCENT = BigDecimal.valueOf(10);
    public static final Short WHOLESALE_COUNT = 5;

    public static void configApp(String[] args) {
        saveToFile = Arrays.stream(args).map(s -> s.split(REGEX_PAIR))
                .filter(strings -> strings.length == 2)
                .filter(strings -> strings[0].equals(SAVE_TO_FILE))
                .map(strings -> strings[1])
                .findFirst()
                .orElseThrow(() -> new CheckRunnerException("BAD REQUEST"));
        pathToProductFile = Arrays.stream(args).map(s -> s.split(REGEX_PAIR))
                .filter(strings -> strings.length == 2)
                .filter(strings -> strings[0].equals(PATH_TO_FILE))
                .map(strings -> strings[1])
                .findFirst()
                .orElseThrow(() -> new CheckRunnerException("BAD REQUEST"));
    }
}