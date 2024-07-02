package ru.clevertec.check.services.api;

import ru.clevertec.check.model.Order;

import java.math.BigDecimal;

public interface CheckService {
    String getCheck(Order order);

    void printCheck(Order order, BigDecimal money);
}
