package ru.clevertec.check.utils;

import ru.clevertec.check.exception.CheckRunnerException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


public class CsvUtil {
    private CsvUtil() {
    }

    public static List<List<String>> readFromCSV(String csvFile, String commaDelimiter) {
        try {
            return Files.readAllLines(Paths.get(csvFile)).stream()
                    .map(line -> Arrays.asList(line.split(commaDelimiter)))
                    .toList();
        } catch (IOException e) {
            throw new CheckRunnerException("INTERNAL SERVER ERROR");
        }
    }

    public static String convertListToCSVString(List<List<String>> lists, String commaDelimiter) {
        StringBuilder stringBuilder = new StringBuilder();
        for (List<String> list : lists) {
            if (list.isEmpty()) {
                stringBuilder.append(commaDelimiter);
            }
            for (String string : list) {
                stringBuilder.append(string);
                stringBuilder.append(commaDelimiter);
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("\n");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public static void filePrint(String csvFileName, String string) {
        try (FileWriter fileWriter = new FileWriter(csvFileName, false)) {
            fileWriter.write(string);
        } catch (IOException e) {
            throw new CheckRunnerException("INTERNAL SERVER ERROR");
        }
    }
}