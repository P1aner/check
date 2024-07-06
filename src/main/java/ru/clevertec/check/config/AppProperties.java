package ru.clevertec.check.config;

import ru.clevertec.check.exception.CheckRunnerException;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.clevertec.check.config.Constants.DATASOURCE_PASSWORD;
import static ru.clevertec.check.config.Constants.DATASOURCE_URL;
import static ru.clevertec.check.config.Constants.DATASOURCE_USERNAME;
import static ru.clevertec.check.config.Constants.SAVE_TO_FILE;
import static ru.clevertec.check.controllers.ConsoleController.REGEX_PAIR;

public class AppProperties {

    public static String saveToFile = "./result.csv";
    public static String datasourceUsername;
    public static String datasourcePassword;
    public static String datasourceUrl;

    private AppProperties() {
    }

    public static void appConfig(String[] args) {
        Map<String, String> parameters = getParameters(args);
        saveToFile = getParameter(parameters, SAVE_TO_FILE);
        datasourceUsername = getParameter(parameters, DATASOURCE_USERNAME);
        datasourcePassword = getParameter(parameters, DATASOURCE_PASSWORD);
        datasourceUrl = getParameter(parameters, DATASOURCE_URL);
    }

    private static String getParameter(Map<String, String> parameters, String parameterName) {
        return Optional.ofNullable(parameters.get(parameterName))
                .orElseThrow(() -> new CheckRunnerException("BAD REQUEST"));
    }

    private static Map<String, String> getParameters(String[] args) {
        return Arrays.stream(args)
                .map(s -> s.split(REGEX_PAIR))
                .filter(strings -> strings.length == 2)
                .collect(Collectors.toMap(strings -> strings[0], strings -> strings[1]));
    }
}