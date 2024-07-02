package ru.clevertec.check.services;

import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.OrderItem;
import ru.clevertec.check.repository.DiscountCardRepositoryImpl;
import ru.clevertec.check.repository.ProductRepositoryImpl;
import ru.clevertec.check.repository.api.DiscountCardRepository;
import ru.clevertec.check.repository.api.ProductRepository;
import ru.clevertec.check.services.api.OrderItemService;
import ru.clevertec.check.services.api.OrderService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class OrderServiceImpl implements OrderService {
    private static final Logger logger = Logger.getLogger("ru.clevertec.check.CheckRunner");

    private final OrderItemService orderItemService = new OrderItemServiceImpl();
    private final ProductRepository productRepository = ProductRepositoryImpl.getInstance();
    private final DiscountCardRepository discountCardRepository = DiscountCardRepositoryImpl.getInstance();

    public Order createOrder(List<String[]> productsArgs, String discountCardId) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (String[] strings : productsArgs) {
            productRepository.findById(Long.parseLong(strings[0]))
                    .ifPresentOrElse(product -> orderItems.add(new OrderItem(product, Integer.parseInt(strings[1]))),
                            () -> {
                                logger.warning("BAD REQUEST");
                                throw new RuntimeException();
                            });
        }
        DiscountCard discountCard = discountCardRepository.findById(Long.parseLong(discountCardId)).stream()
                .findFirst()
                .orElse(null);
        return new Order(orderItems, discountCard);
    }

    public BigDecimal calculateOrderTotalPrice(Order order) {
        return order.getOrderItems().stream()
                .map(orderItemService::calculateOrderItemTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateOrderTotalDiscount(Order order) {
        return order.getOrderItems().stream()
                .map(orderItem -> orderItemService.calculateOrderItemDiscount(orderItem, order))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateOrderTotalWithDiscount(Order order) {
        return calculateOrderTotalPrice(order)
                .subtract(calculateOrderTotalDiscount(order))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isEnoughMoney(Order order, BigDecimal money) {
        boolean b = money.compareTo(calculateOrderTotalWithDiscount(order)) > 0;
        if (!b) {
            logger.warning("NOT ENOUGH MONEY");
            return false;
        } else return true;
    }
}