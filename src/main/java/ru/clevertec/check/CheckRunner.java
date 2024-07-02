package ru.clevertec.check;

import ru.clevertec.check.config.SimpleLoggerFormatter;
import ru.clevertec.check.controllers.ConsoleController;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import static ru.clevertec.check.config.AppConfig.SAVE_TO_FILE;

public class CheckRunner {
    private static final Logger logger = Logger.getLogger("ru.clevertec.check.CheckRunner");

    public static void main(String[] args) throws IOException {
        FileHandler fileHandler = new FileHandler(SAVE_TO_FILE);
        fileHandler.setFormatter(new SimpleLoggerFormatter());
        logger.addHandler(fileHandler);
        logger.setUseParentHandlers(false);

        ConsoleController consoleController = new ConsoleController();
        consoleController.create(args);
    }
}