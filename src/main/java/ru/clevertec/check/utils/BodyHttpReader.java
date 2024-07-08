package ru.clevertec.check.utils;

import jakarta.servlet.http.HttpServletRequest;
import ru.clevertec.check.exception.CheckRunnerException;

import java.io.IOException;

public class BodyHttpReader {
    private BodyHttpReader() {
    }

    public static String read(HttpServletRequest req) {
        try {
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = req.getReader().readLine()) != null) {
                body.append(line);
            }
            return body.toString();
        } catch (IOException e) {
            throw CheckRunnerException.internalServerError();
        }
    }
}
