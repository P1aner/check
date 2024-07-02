package ru.clevertec.check.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


public class CSVReader {
    private static final Logger logger = Logger.getLogger("ru.clevertec.check.CheckRunner");

    public List<List<String>> readFromCSV(String CSV_FILE, String COMMA_DELIMITER) {
        try {
            return Files.readAllLines(Paths.get(CSV_FILE)).stream()
                    .map(line -> Arrays.asList(line.split(COMMA_DELIMITER)))
                    .toList();
        } catch (IOException e) {
            logger.warning("INTERNAL SERVER ERROR");
            throw new RuntimeException();
        }
    }

    public String convertListToCSVString(List<List<String>> lists, String COMMA_DELIMITER) {
        StringBuilder stringBuilder = new StringBuilder();
        for (List<String> list : lists) {
            for (String string : list) {
                stringBuilder.append(string);
                stringBuilder.append(COMMA_DELIMITER);
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public void filePrint(String CSV_FILE_NAME, String string) {
        try (FileWriter fileWriter = new FileWriter(CSV_FILE_NAME, false)) {
            fileWriter.write(string);
        } catch (IOException e) {
            logger.warning("INTERNAL SERVER ERROR");
            throw new RuntimeException();
        }
    }
}