package ru.clevertec.check.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.dto.DiscountCardDTO;
import ru.clevertec.check.dto.mapper.DiscountCardMapper;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.DebitCardException;
import ru.clevertec.check.exception.ObjectNotFoundException;
import ru.clevertec.check.repository.DiscountCardRepositorySqL;
import ru.clevertec.check.repository.api.DiscountCardRepository;
import ru.clevertec.check.services.DiscountCardServiceBase;
import ru.clevertec.check.services.api.DiscountCardService;
import ru.clevertec.check.utils.BodyHttpReader;

import java.io.PrintWriter;

@WebServlet(urlPatterns = "/discountcards")
public class DiscountCardController extends HttpServlet {
    private DiscountCardService discountCardService;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        DiscountCardRepository discountCardRepository = DiscountCardRepositorySqL.getInstance();
        DiscountCardMapper discountCardMapper = new DiscountCardMapper();
        this.discountCardService = new DiscountCardServiceBase(discountCardRepository, discountCardMapper);
        this.objectMapper = new ObjectMapper();
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String discountCardId = req.getParameter("id");
            DiscountCardDTO discountCardDTO = discountCardService.getDiscountCard(discountCardId);
            String jsonDiscountCard = objectMapper.writeValueAsString(discountCardDTO);
            resp.setContentType("text/json");
            PrintWriter out = resp.getWriter();
            out.println(jsonDiscountCard);
        } catch (BadRequestException | DebitCardException e) {
            resp.setStatus(400);
        } catch (ObjectNotFoundException e) {
            resp.setStatus(404);
        } catch (Exception e) {
            resp.setStatus(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String postBody = BodyHttpReader.read(req);
            DiscountCardDTO discountCardDTO = objectMapper.readValue(postBody, DiscountCardDTO.class);
            discountCardService.createNewDiscountCard(discountCardDTO);
        } catch (BadRequestException | DebitCardException e) {
            resp.setStatus(400);
        } catch (ObjectNotFoundException e) {
            resp.setStatus(404);
        } catch (Exception e) {
            resp.setStatus(500);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String discountCardId = req.getParameter("id");
            String postBody = BodyHttpReader.read(req);
            DiscountCardDTO discountCardDTO = objectMapper.readValue(postBody, DiscountCardDTO.class);
            discountCardService.updateDiscountCard(discountCardId, discountCardDTO);
        } catch (BadRequestException | DebitCardException e) {
            resp.setStatus(400);
        } catch (ObjectNotFoundException e) {
            resp.setStatus(404);
        } catch (Exception e) {
            resp.setStatus(500);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String discountCardId = req.getParameter("id");
            discountCardService.deleteDiscountCard(discountCardId);
            resp.setStatus(204);
        } catch (BadRequestException | DebitCardException e) {
            resp.setStatus(400);
        } catch (ObjectNotFoundException e) {
            resp.setStatus(404);
        } catch (Exception e) {
            resp.setStatus(500);
        }
    }
}