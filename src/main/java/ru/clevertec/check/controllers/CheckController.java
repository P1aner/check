package ru.clevertec.check.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.dto.OrderDTO;
import ru.clevertec.check.dto.OrderItemDTO;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.exception.DebitCardException;
import ru.clevertec.check.exception.ObjectNotFoundException;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.repository.DiscountCardRepositorySqL;
import ru.clevertec.check.repository.ProductRepositorySqL;
import ru.clevertec.check.repository.api.DiscountCardRepository;
import ru.clevertec.check.repository.api.ProductRepository;
import ru.clevertec.check.services.CheckServiceBase;
import ru.clevertec.check.services.OrderItemServiceBase;
import ru.clevertec.check.services.OrderServiceBase;
import ru.clevertec.check.services.api.CheckService;
import ru.clevertec.check.services.api.OrderItemService;
import ru.clevertec.check.services.api.OrderService;
import ru.clevertec.check.utils.BodyHttpReader;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/check")
public class CheckController extends HttpServlet {
    private CheckService checkService;
    private OrderService orderService;
    private ObjectMapper objectMapper;


    @Override
    public void init(ServletConfig config) throws ServletException {
        OrderItemService orderItemService = new OrderItemServiceBase();
        ProductRepository productRepository = ProductRepositorySqL.getInstance();
        DiscountCardRepository discountCardRepository = DiscountCardRepositorySqL.getInstance();
        this.orderService = new OrderServiceBase(orderItemService, productRepository, discountCardRepository);
        this.checkService = new CheckServiceBase(orderService, orderItemService);
        this.objectMapper = new ObjectMapper();
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String postBody = BodyHttpReader.read(req);
            OrderDTO orderDTO = objectMapper.readValue(postBody, OrderDTO.class);

            String discountCardId = String.valueOf(orderDTO.getDiscountCard());
            BigDecimal balanceDebitCard = orderDTO.getBalanceDebitCard();

            Map<Long, Integer> collect = orderDTO.getProducts().stream()
                    .collect(Collectors.groupingBy(OrderItemDTO::getId, Collectors.summingInt(OrderItemDTO::getQuantity)));
            if (collect.isEmpty()) {
                throw CheckRunnerException.internalServerError();
            }
            Order order = orderService.createOrder(collect, discountCardId);
            String check = checkService.getCheck(order);

            orderService.completeOrder(order, balanceDebitCard);
            PrintWriter out = resp.getWriter();
            out.println(check);
            resp.setHeader("Content-Disposition", "attachment; filename=result.csv");
        } catch (BadRequestException | DebitCardException e) {
            resp.setStatus(400);
        } catch (ObjectNotFoundException e) {
            resp.setStatus(404);
        } catch (Exception e) {
            resp.setStatus(500);
        }
    }
}