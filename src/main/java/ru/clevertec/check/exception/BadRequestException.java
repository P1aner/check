package ru.clevertec.check.exception;

public class BadRequestException extends CheckRunnerException {
    public BadRequestException(String message) {
        super(message);
    }
}
