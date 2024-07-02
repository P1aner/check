package ru.clevertec.check.config;

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class SimpleLoggerFormatter extends SimpleFormatter {
    @Override
    public synchronized String format(LogRecord lr) {
        return String.format("ERROR\n%s",
                lr.getMessage());
    }
}
