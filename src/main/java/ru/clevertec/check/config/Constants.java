package ru.clevertec.check.config;

import java.math.BigDecimal;

public class Constants {
    public static final String DATASOURCE_USERNAME = "datasource.username";
    public static final String DATASOURCE_PASSWORD = "datasource.password";
    public static final String DATASOURCE_URL = "datasource.url";
    public static final String SAVE_TO_FILE = "saveToFile";
    public static final String CSV_DELIMITER = ";";
    public static final BigDecimal WHOLESALE_PERCENT = BigDecimal.valueOf(10);
    public static final Integer WHOLESALE_COUNT = 5;

    private Constants(){}
}
