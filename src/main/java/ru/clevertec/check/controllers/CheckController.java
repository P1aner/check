package ru.clevertec.check.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.clevertec.check.services.CheckServiceBase;
import ru.clevertec.check.services.OrderServiceBase;
import ru.clevertec.check.services.api.CheckService;
import ru.clevertec.check.services.api.OrderService;
import ru.clevertec.check.utils.BodyHttpReader;

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