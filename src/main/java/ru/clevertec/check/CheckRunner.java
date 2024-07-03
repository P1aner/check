package ru.clevertec.check;

import ru.clevertec.check.controllers.ConsoleController;

import static ru.clevertec.check.config.AppConfig.configApp;
import static ru.clevertec.check.config.AppConfig.configLogger;

public class CheckRunner {

    public static void main(String[] args) {
        configLogger();
        configApp(args);

        ConsoleController consoleController = new ConsoleController();
        consoleController.create(args);
    }
}