package ru.clevertec.check.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.dto.ProductDTO;
import ru.clevertec.check.services.ProductServiceBase;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/products")
public class ProductController extends HttpServlet {
    private final ProductServiceBase productsService = new ProductServiceBase();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("id");
        ProductDTO productDTO = productsService.getProduct(productId);
        String jsonProduct = objectMapper.writeValueAsString(productDTO);
        resp.setContentType("text/json");
        PrintWriter out = resp.getWriter();
        out.println(jsonProduct);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            body.append(line);
        }
        String postBody = body.toString();
        ProductDTO productDTO = objectMapper.readValue(postBody, ProductDTO.class);
        productsService.createNewProduct(productDTO);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("id");
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            body.append(line);
        }
        String postBody = body.toString();
        ProductDTO productDTO = objectMapper.readValue(postBody, ProductDTO.class);
        productsService.updateProduct(productDTO, productId);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("id");
        productsService.deleteProduct(productId);
    }
}