package ru.clevertec.check;

import ru.clevertec.check.controllers.ConsoleController;
import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.repository.DiscountCardRepositoryCsV;
import ru.clevertec.check.repository.ProductRepositoryCsV;
import ru.clevertec.check.repository.api.DiscountCardRepository;
import ru.clevertec.check.repository.api.ProductRepository;
import ru.clevertec.check.services.CheckServiceBase;
import ru.clevertec.check.services.OrderItemServiceBase;
import ru.clevertec.check.services.OrderServiceBase;
import ru.clevertec.check.services.api.CheckService;
import ru.clevertec.check.services.api.OrderItemService;
import ru.clevertec.check.services.api.OrderService;
import ru.clevertec.check.utils.CsvUtil;

import static ru.clevertec.check.config.AppProperties.configApp;
import static ru.clevertec.check.config.AppProperties.saveToFile;

public class CheckRunner {

    public static void main(String[] args) {
        DiscountCardRepository discountCardRepository = DiscountCardRepositoryCsV.getInstance();
        ProductRepository productRepository = ProductRepositoryCsV.getInstance();
        OrderItemService orderItemService = new OrderItemServiceBase();
        OrderService orderService = new OrderServiceBase(orderItemService, productRepository, discountCardRepository);
        CheckService checkService = new CheckServiceBase(orderService, orderItemService);
        ConsoleController consoleController = new ConsoleController(orderService, checkService);
        try {
            configApp(args);
            ConsoleController consoleController = new ConsoleController();
            consoleController.create(args);
        } catch (CheckRunnerException e) {
            CsvUtil.filePrint(saveToFile, String.format("ERROR%n%s", e.getMessage()));
        }
    }
}