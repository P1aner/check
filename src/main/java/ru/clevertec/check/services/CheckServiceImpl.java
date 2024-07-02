package ru.clevertec.check.services;

import ru.clevertec.check.model.Order;
import ru.clevertec.check.services.api.CheckService;
import ru.clevertec.check.services.api.OrderItemService;
import ru.clevertec.check.services.api.OrderService;
import ru.clevertec.check.utils.CSVReader;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ru.clevertec.check.config.AppConfig.CSV_DELIMITER;
import static ru.clevertec.check.config.AppConfig.SAVE_TO_FILE;
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

public class CheckServiceImpl implements CheckService {
    private final OrderService orderService = new OrderServiceImpl();
    private final OrderItemService orderItemService = new OrderItemServiceImpl();
    private final CSVReader csvReader = new CSVReader();

    private final static DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final static DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    public String getCheck(Order order) {
        return csvReader.convertListToCSVString(orderToLists(order), CSV_DELIMITER);
    }

    public void printCheck(Order order, BigDecimal money) {
        if (orderService.isEnoughMoney(order, money)) {
            csvReader.filePrint(SAVE_TO_FILE, getCheck(order));
        }
    }

    private List<List<String>> orderToLists(Order order) {

        List<List<String>> lists = new ArrayList<>();
        lists.add(List.of(DATE, TIME));
        lists.add(List.of(LocalDate.now().format(FORMATTER_DATE), LocalDateTime.now().format(FORMATTER_TIME)));
        lists.add(new ArrayList<>());
        lists.add(List.of(QTY, DESCRIPTION, PRICE, DISCOUNT, TOTAL));
        order.getOrderItems()
                .forEach(orderItem -> lists.add(List.of(
                        orderItem.getCount().toString(),
                        orderItem.getProduct().getDescription(),
                        orderItem.getProduct().getPrice().toString() + "$",
                        orderItemService.calculateOrderItemDiscount(orderItem, order).toString() + "$",
                        orderItemService.calculateOrderItemTotalPrice(orderItem).toString() + "$")));
        lists.add(new ArrayList<>());
        if (order.getDiscountCard() != null) {
            lists.add(List.of(DISCOUNT_CARD, DISCOUNT_PERCENTAGE));
            lists.add(List.of(order.getDiscountCard().getNumber().toString(), order.getDiscountCard().getAmount() + "%"));
            lists.add(new ArrayList<>());
        }
        lists.add(List.of(TOTAL_PRICE, TOTAL_DISCOUNT, TOTAL_WITH_DISCOUNT));
        lists.add(List.of(orderService.calculateOrderTotalPrice(order).toString() + "$",
                orderService.calculateOrderTotalDiscount(order).toString() + "$",
                orderService.calculateOrderTotalWithDiscount(order).toString() + "$"));
        return lists;
    }
}