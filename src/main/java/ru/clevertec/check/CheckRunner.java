package ru.clevertec.check;

import ru.clevertec.check.controllers.ConsoleController;
import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.utils.CsvUtil;

import static ru.clevertec.check.config.AppProperties.SAVE_TO_FILE;

public class CheckRunner {

    public static void main(String[] args) {
        ConsoleController consoleController = new ConsoleController();
        try {
            consoleController.create(args);
        } catch (CheckRunnerException e) {
            CsvUtil.filePrint(SAVE_TO_FILE, String.format("ERROR%n%s", e.getMessage()));
        }
    }
}