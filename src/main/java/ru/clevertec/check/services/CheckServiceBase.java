package ru.clevertec.check.services;

import ru.clevertec.check.model.Order;
import ru.clevertec.check.services.api.CheckService;
import ru.clevertec.check.services.api.OrderItemService;
import ru.clevertec.check.services.api.OrderService;
import ru.clevertec.check.utils.CsvUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ru.clevertec.check.config.AppProperties.CSV_DELIMITER;
import static ru.clevertec.check.config.AppProperties.saveToFile;
import static ru.clevertec.check.config.DefaultMessages.DATE;
import static ru.clevertec.check.config.DefaultMessages.DESCRIPTION;
import static ru.clevertec.check.config.DefaultMessages.DISCOUNT;
import static ru.clevertec.check.config.DefaultMessages.DISCOUNT_CARD;
import static ru.clevertec.check.config.DefaultMessages.DISCOUNT_PERCENTAGE;
import static ru.clevertec.check.config.DefaultMessages.PRICE;
import static ru.clevertec.check.config.DefaultMessages.QTY;
import static ru.clevertec.check.config.DefaultMessages.TIME;
import static ru.clevertec.check.config.DefaultMessages.TOTAL;
import static ru.clevertec.check.config.DefaultMessages.TOTAL_DISCOUNT;
import static ru.clevertec.check.config.DefaultMessages.TOTAL_PRICE;
import static ru.clevertec.check.config.DefaultMessages.TOTAL_WITH_DISCOUNT;

public class CheckServiceBase implements CheckService {
    private static final List<String> DISCOUNT_ROW = List.of(DISCOUNT_CARD, DISCOUNT_PERCENTAGE);
    private static final List<String> TOTAL_ROW = List.of(TOTAL_PRICE, TOTAL_DISCOUNT, TOTAL_WITH_DISCOUNT);
    private static final String PERCENT_SIGN = "%";
    private static final String DOLLAR_SIGN = "$";
    private static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final OrderService orderService = new OrderServiceBase();
    private final OrderItemService orderItemService = new OrderItemServiceBase();


    @Override
    public String getCheck(Order order) {
        return CsvUtil.convertListToCSVString(orderToLists(order), CSV_DELIMITER);
    }

    @Override
    public void printCheck(Order order, BigDecimal money) {
        if (orderService.isEnoughMoney(order, money)) {
            CsvUtil.filePrint(saveToFile, getCheck(order));
        }
    }

    private List<List<String>> orderToLists(Order order) {
        List<List<String>> lists = new ArrayList<>();
        lists.add(List.of(DATE, TIME));
        lists.add(List.of(LocalDate.now().format(FORMATTER_DATE), LocalDateTime.now().format(FORMATTER_TIME)));
        lists.add(new ArrayList<>());
        lists.add(List.of(QTY, DESCRIPTION, PRICE, DISCOUNT, TOTAL));
        order.getOrderItems().forEach(orderItem ->
                lists.add(
                        List.of(
                                orderItem.getCount().toString(),
                                orderItem.getProduct().getDescription(),
                                orderItem.getProduct().getPrice() + DOLLAR_SIGN,
                                orderItemService.calculateOrderItemDiscount(orderItem, order) + DOLLAR_SIGN,
                                orderItemService.calculateOrderItemTotalPrice(orderItem) + DOLLAR_SIGN)));
        lists.add(new ArrayList<>());
        if (order.getDiscountCard() != null) {
            lists.add(DISCOUNT_ROW);
            lists.add(List.of(
                    order.getDiscountCard().getNumber().toString(),
                    order.getDiscountCard().getAmount() + PERCENT_SIGN));
            lists.add(new ArrayList<>());
        }
        lists.add(TOTAL_ROW);
        lists.add(List.of(orderService.calculateOrderTotalPrice(order) + DOLLAR_SIGN,
                orderService.calculateOrderTotalDiscount(order) + DOLLAR_SIGN,
                orderService.calculateOrderTotalWithDiscount(order) + DOLLAR_SIGN));
        return lists;
    }
}