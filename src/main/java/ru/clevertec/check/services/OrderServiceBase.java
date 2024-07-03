package ru.clevertec.check.services;

import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.OrderItem;
import ru.clevertec.check.repository.DiscountCardRepositoryCSV;
import ru.clevertec.check.repository.ProductRepositoryCSV;
import ru.clevertec.check.repository.api.DiscountCardRepository;
import ru.clevertec.check.repository.api.ProductRepository;
import ru.clevertec.check.services.api.OrderItemService;
import ru.clevertec.check.services.api.OrderService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceBase implements OrderService {
    private final OrderItemService orderItemService = new OrderItemServiceBase();
    private final ProductRepository productRepository = ProductRepositoryCSV.getInstance();
    private final DiscountCardRepository discountCardRepository = DiscountCardRepositoryCSV.getInstance();

    @Override
    public Order createOrder(List<String[]> productsArgs, String discountCardId) {
        int discountCardIdInt = Integer.parseInt(discountCardId);
        List<OrderItem> orderItems = new ArrayList<>();
        for (String[] strings : productsArgs) {
            long id = Long.parseLong(strings[0]);
            productRepository.findById(id)
                    .ifPresentOrElse(product -> orderItems.add(new OrderItem(product, Integer.parseInt(strings[1]))),
                            () -> {
                                throw new CheckRunnerException("BAD REQUEST");
                            });
        }
        DiscountCard discountCard = discountCardRepository.findByNumber(discountCardIdInt)
                .stream()
                .findFirst()
                .orElse(null);
        return new Order(orderItems, discountCard);
    }

    @Override
    public BigDecimal calculateOrderTotalPrice(Order order) {
        return order.getOrderItems().stream()
                .map(orderItemService::calculateOrderItemTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateOrderTotalDiscount(Order order) {
        return order.getOrderItems().stream()
                .map(orderItem -> orderItemService.calculateOrderItemDiscount(orderItem, order))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateOrderTotalWithDiscount(Order order) {
        return calculateOrderTotalPrice(order)
                .subtract(calculateOrderTotalDiscount(order))
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean isEnoughMoney(Order order, BigDecimal money) {
        if (money.compareTo(calculateOrderTotalWithDiscount(order)) > 0) {
            return true;
        }
        throw new CheckRunnerException("NOT ENOUGH MONEY");
    }
}