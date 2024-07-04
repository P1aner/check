package ru.clevertec.check.config;

import ru.clevertec.check.exception.CheckRunnerException;

import java.math.BigDecimal;
import java.util.Arrays;

import static ru.clevertec.check.controllers.ConsoleController.REGEX_PAIR;

public class AppConfig {

    public static final String DATASOURCE_USERNAME = "datasource.username";
    public static final String DATASOURCE_PASSWORD = "datasource.password";
    public static final String DATASOURCE_URL = "datasource.url";

    private AppConfig() {
    }

    public static final String SAVE_TO_FILE = "saveToFile";
    public static final String CSV_DELIMITER = ";";
    public static String pathToProductFile = "./src/main/resources/products.csv";
    public static String saveToFile = "./result.csv";
    public static final String PATH_TO_DISCOUNT_CARD_FILE = "./src/main/resources/discountCards.csv";
    public static final BigDecimal WHOLESALE_PERCENT = BigDecimal.valueOf(10);
    public static final Short WHOLESALE_COUNT = 5;
    public static String datasourceUsername;
    public static String datasourcePassword;
    public static String datasourceUrl;

    public static void configApp(String[] args) {
        saveToFile = getParameter(args, SAVE_TO_FILE);
        datasourceUsername = getParameter(args, DATASOURCE_USERNAME);
        datasourcePassword = getParameter(args, DATASOURCE_PASSWORD);
        datasourceUrl = getParameter(args, DATASOURCE_URL);
    }

    private static String getParameter(String[] args, String aa) {
        return Arrays.stream(args).map(s -> s.split(REGEX_PAIR))
                .filter(strings -> strings.length == 2)
                .filter(strings -> strings[0].equals(aa))
                .map(strings -> strings[1])
                .findFirst()
                .orElseThrow(() -> new CheckRunnerException("BAD REQUEST"));
    }
}