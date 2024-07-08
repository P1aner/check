package ru.clevertec.check.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.dto.OrderDTO;
import ru.clevertec.check.dto.OrderItemDTO;
import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.services.CheckServiceBase;
import ru.clevertec.check.services.OrderServiceBase;
import ru.clevertec.check.services.api.CheckService;
import ru.clevertec.check.services.api.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/check")
public class CheckController extends HttpServlet {
    private final CheckService checkService = new CheckServiceBase();
    private final OrderService orderService = new OrderServiceBase();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            body.append(line);
        }
        String postBody = body.toString();
        OrderDTO orderDTO = objectMapper.readValue(postBody, OrderDTO.class);

        String discountCardId = String.valueOf(orderDTO.getDiscountCard());
        BigDecimal balanceDebitCard = orderDTO.getBalanceDebitCard();

        Map<Long, Integer> collect = orderDTO.getProducts().stream()
                .collect(Collectors.groupingBy(OrderItemDTO::getId, Collectors.summingInt(OrderItemDTO::getQuantity)));
        if (collect.isEmpty()) {
            throw new CheckRunnerException("INTERNAL SERVER ERROR");
        }
        Order order = orderService.createOrder(collect, discountCardId);
        String check = checkService.getCheck(order);

        orderService.completeOrder(order, balanceDebitCard);
        PrintWriter out = resp.getWriter();
        out.println(check);
        resp.setHeader("Content-Disposition", "attachment; filename=result.csv");
    }
}