package ru.clevertec.check.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.dto.DiscountCardDTO;
import ru.clevertec.check.services.DiscountCardServiceBase;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/discountcards")
public class DiscountCardController extends HttpServlet {
    private final DiscountCardServiceBase discountCardService = new DiscountCardServiceBase();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String discountCardId = req.getParameter("id");
        DiscountCardDTO discountCardDTO = discountCardService.getDiscountCard(discountCardId);
        String jsonDiscountCard = objectMapper.writeValueAsString(discountCardDTO);
        resp.setContentType("text/json");
        PrintWriter out = resp.getWriter();
        out.println(jsonDiscountCard);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            body.append(line);
        }
        String postBody = body.toString();
        DiscountCardDTO discountCardDTO = objectMapper.readValue(postBody, DiscountCardDTO.class);
        discountCardService.createNewDiscountCard(discountCardDTO);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String discountCardId = req.getParameter("id");
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            body.append(line);
        }
        String postBody = body.toString();
        DiscountCardDTO discountCardDTO = objectMapper.readValue(postBody, DiscountCardDTO.class);
        discountCardService.updateDiscountCard(discountCardDTO, discountCardId);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String discountCardId = req.getParameter("id");
        discountCardService.deleteDiscountCard(discountCardId);
    }
}