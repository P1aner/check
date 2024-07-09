package ru.clevertec.check.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.dto.ProductDTO;
import ru.clevertec.check.dto.mapper.ProductMapper;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.DebitCardException;
import ru.clevertec.check.exception.ObjectNotFoundException;
import ru.clevertec.check.repository.ProductRepositorySqL;
import ru.clevertec.check.repository.api.ProductRepository;
import ru.clevertec.check.services.ProductServiceBase;
import ru.clevertec.check.services.api.ProductService;
import ru.clevertec.check.utils.BodyHttpReader;

import java.io.PrintWriter;

@WebServlet(urlPatterns = "/products")
public class ProductController extends HttpServlet {
    private ProductService productService;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ProductRepository productRepository = ProductRepositorySqL.getInstance();
        ProductMapper productMapper = new ProductMapper();
        this.productService = new ProductServiceBase(productRepository, productMapper);
        this.objectMapper = new ObjectMapper();
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String productId = req.getParameter("id");
            ProductDTO productDTO = productService.getProduct(productId);
            String jsonProduct = objectMapper.writeValueAsString(productDTO);
            resp.setContentType("text/json");
            PrintWriter out = resp.getWriter();
            out.println(jsonProduct);
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
            ProductDTO productDTO = objectMapper.readValue(postBody, ProductDTO.class);
            productService.createNewProduct(productDTO);
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
            String productId = req.getParameter("id");
            String postBody = BodyHttpReader.read(req);
            ProductDTO productDTO = objectMapper.readValue(postBody, ProductDTO.class);
            productService.updateProduct(productId, productDTO);
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
            String productId = req.getParameter("id");
            productService.deleteProduct(productId);
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