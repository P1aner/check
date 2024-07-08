package ru.clevertec.check.exception;

public class CheckRunnerException extends RuntimeException {

    private static final String INTERNAL_SERVER_ERROR = "INTERNAL SERVER ERROR";

    public CheckRunnerException(String message) {
        super(message);
    }

    public static CheckRunnerException internalServerError() {
        return new CheckRunnerException(INTERNAL_SERVER_ERROR);
    }
}