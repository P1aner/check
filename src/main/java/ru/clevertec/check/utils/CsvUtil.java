package ru.clevertec.check.utils;

import java.util.List;


public class CsvUtil {
    private CsvUtil() {
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
}