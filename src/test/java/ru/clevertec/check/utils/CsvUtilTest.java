package ru.clevertec.check.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static ru.clevertec.check.config.AppConfig.CSV_DELIMITER;

class CsvUtilTest {

    @Test
    void convertListToCSVString() {
        List<List<String>> lists = List.of(List.of("1", "2"), List.of("1", "2"), new ArrayList<>());
        String s = CsvUtil.convertListToCSVString(lists, CSV_DELIMITER);
        Assertions.assertEquals("1;2\n1;2\n", s);
    }
}