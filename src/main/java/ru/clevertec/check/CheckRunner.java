package ru.clevertec.check;

import ru.clevertec.check.controllers.ConsoleController;
import ru.clevertec.check.exception.CheckRunnerException;


import static ru.clevertec.check.config.AppConfig.SAVE_TO_FILE;

import static ru.clevertec.check.config.AppConfig.configApp;

public class CheckRunner {

    public static void main(String[] args) {
        configApp(args);

        ConsoleController consoleController = new ConsoleController();
        try {
            consoleController.create(args);
        } catch (CheckRunnerException e) {
            CsvUtil.filePrint(SAVE_TO_FILE, String.format("ERROR%n%s", e.getMessage()));
        }
    }
}