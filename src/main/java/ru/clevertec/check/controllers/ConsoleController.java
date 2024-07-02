package ru.clevertec.check.controllers;

import ru.clevertec.check.model.Order;
import ru.clevertec.check.services.CheckServiceImpl;
import ru.clevertec.check.services.OrderServiceImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static ru.clevertec.check.utils.NumberCheck.isNumber;

public class ConsoleController {
    private static final Logger logger = Logger.getLogger("ru.clevertec.check.CheckRunner");

    private final OrderServiceImpl orderServiceImpl = new OrderServiceImpl();
    private final CheckServiceImpl checkServiceImpl = new CheckServiceImpl();

    public void create(String[] args) {
        List<String[]> list = Arrays.stream(args)
                .map(string -> string.split("="))
                .toList();

        String discountCardId = list.stream()
                .filter(strings -> strings.length == 2)
                .filter(strings -> strings[0].equals("discountCard"))
                .map(strings -> strings[1])
                .findFirst()
                .orElse(String.valueOf(0));

        Optional<BigDecimal> balanceDebitCard = list.stream()
                .filter(strings -> strings.length == 2)
                .filter(strings -> strings[0].equals("balanceDebitCard"))
                .map(strings -> new BigDecimal(strings[1]))
                .findFirst();

        List<String[]> products = Arrays.stream(args)
                .map(string -> string.split("-"))
                .filter(strings -> strings.length == 2)
                .filter(strings -> isNumber(strings[0]))
                .toList();
        Order order = orderServiceImpl.createOrder(products, discountCardId);

        BigDecimal money = balanceDebitCard.orElseThrow(() -> {
            logger.warning("INTERNAL SERVER ERROR");
            return new RuntimeException();
        });

        checkServiceImpl.printCheck(order, money);
    }
}