package ru.clevertec.check.services;

import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.OrderItem;
import ru.clevertec.check.repository.DiscountCardRepositoryCsV;
import ru.clevertec.check.repository.ProductRepositoryCsV;
import ru.clevertec.check.repository.api.DiscountCardRepository;
import ru.clevertec.check.repository.api.ProductRepository;
import ru.clevertec.check.services.api.OrderItemService;
import ru.clevertec.check.services.api.OrderService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class OrderServiceBase implements OrderService {
    private final OrderItemService orderItemService = new OrderItemServiceBase();
    private final ProductRepository productRepository = ProductRepositoryCsV.getInstance();
    private final DiscountCardRepository discountCardRepository = DiscountCardRepositoryCsV.getInstance();

    @Override
    public Order createOrder(Map<Long, Integer> productsArgs, String discountCardId) {
        int discountCardIdInt = Integer.parseInt(discountCardId);
        List<OrderItem> orderItems = productsArgs.entrySet().stream()
                .map(s -> {
                    return productRepository.findById(s.getKey())
                            .map(product -> new OrderItem(product, s.getValue()))
                            .orElseThrow(() -> new CheckRunnerException("BAD REQUEST"));
                })
                .toList();
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