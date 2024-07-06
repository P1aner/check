package ru.clevertec.check;

import ru.clevertec.check.controllers.ConsoleController;
import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.utils.CsvUtil;

import static ru.clevertec.check.config.AppProperties.configApp;
import static ru.clevertec.check.config.AppProperties.saveToFile;

public class CheckRunner {

    public static void main(String[] args) {
        try {
            configApp(args);
            ConsoleController consoleController = new ConsoleController();
            consoleController.create(args);
        } catch (CheckRunnerException e) {
            CsvUtil.filePrint(saveToFile, String.format("ERROR%n%s", e.getMessage()));
        }
    }
}