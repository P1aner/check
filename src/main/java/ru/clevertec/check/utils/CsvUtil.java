package ru.clevertec.check.utils;

import java.util.List;


public class CsvUtil {
    private CsvUtil() {
    }

    public static String convertListToCSVString(List<List<String>> lists, String commaDelimiter) {
        StringBuilder stringBuilder = new StringBuilder();
        lists.forEach(list -> {
            if (list.isEmpty()) {
                stringBuilder.append(commaDelimiter);
            } else {
                list.forEach(string -> {
                    stringBuilder.append(string);
                    stringBuilder.append(commaDelimiter);
                });
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("\n");
        });
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}