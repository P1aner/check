package ru.clevertec.check.controllers;

import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.services.api.CheckService;
import ru.clevertec.check.services.api.OrderService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class ConsoleController {
    public static final String NUMBER_REGEX = "-?\\d+(\\.\\d+)?";
    public static final String DISCOUNT_CARD = "discountCard";
    public static final String ZERO_STRING = "0";
    public static final String BALANCE_DEBIT_CARD = "balanceDebitCard";
    public static final String REGEX_PAIR = "=";
    public static final String REGEX_DASH = "-";

    private final OrderService orderService;
    private final CheckService checkService;

    public ConsoleController(OrderService orderService, CheckService checkService) {
        this.orderService = orderService;
        this.checkService = checkService;
    }

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