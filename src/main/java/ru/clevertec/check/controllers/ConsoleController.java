package ru.clevertec.check.controllers;

import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.services.CheckServiceBase;
import ru.clevertec.check.services.OrderServiceBase;
import ru.clevertec.check.services.api.CheckService;
import ru.clevertec.check.services.api.OrderService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ConsoleController {
    private static final String NUMBER_REGEX = "-?\\d+(\\.\\d+)?";
    private static final String DISCOUNT_CARD = "discountCard";
    private static final String ZERO_STRING = "0";
    private static final String BALANCE_DEBIT_CARD = "balanceDebitCard";
    private static final String REGEX_PAIR = "=";
    private static final String REGEX_DASH = "-";

    private final OrderService orderService = new OrderServiceBase();
    private final CheckService checkService = new CheckServiceBase();

    public void create(String[] args) {
        List<String[]> list = Arrays.stream(args)
                .map(string -> string.split(REGEX_PAIR))
                .toList();

        String discountCardId = list.stream()
                .filter(strings -> strings.length == 2)
                .filter(strings -> DISCOUNT_CARD.equals(strings[0]))
                .map(strings -> strings[1])
                .findFirst()
                .orElse(ZERO_STRING);

        BigDecimal money = list.stream()
                .filter(strings -> strings.length == 2)
                .filter(strings -> BALANCE_DEBIT_CARD.equals(strings[0]))
                .map(strings -> new BigDecimal(strings[1]))
                .findFirst()
                .orElseThrow(() -> new CheckRunnerException("INTERNAL SERVER ERROR"));

        Map<Long, Integer> collect = Arrays.stream(args)
                .map(string -> string.split(REGEX_DASH))
                .filter(strings -> strings.length == 2)
                .filter(productIdIsNumberOrElseThrow())
                .collect(Collectors.groupingBy(str -> Long.parseLong(str[0]), Collectors.summingInt(s -> Integer.parseInt(s[1]))));
        if (collect.isEmpty()) {
            throw new CheckRunnerException("INTERNAL SERVER ERROR");
        }
        Order order = orderService.createOrder(collect, discountCardId);

        checkService.printCheck(order, money);
    }

    private static Predicate<String[]> productIdIsNumberOrElseThrow() {
        return strings -> {
            if (strings[0].matches(NUMBER_REGEX) && strings[1].matches(NUMBER_REGEX)) {
                return true;
            }
            throw new CheckRunnerException("INTERNAL SERVER ERROR");
        };
    }
}