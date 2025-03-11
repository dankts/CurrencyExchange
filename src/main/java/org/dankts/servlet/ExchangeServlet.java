package org.dankts.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dankts.dto.ExchangeDto;
import org.dankts.service.ExchangeService;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private final ExchangeService exchangeService = new ExchangeService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrency = req.getParameter("from");
        String targetCurrency = req.getParameter("to");
        BigDecimal amount = new BigDecimal(req.getParameter("amount"));
        PrintWriter writer = resp.getWriter();
        String json;

        ExchangeDto exchange = exchangeService.exchange(baseCurrency, targetCurrency, amount);

        if (exchange == null) {
            ErrorResponse errorResponse = new ErrorResponse("Валюта не найдена");
            json = gson.toJson(errorResponse);
            writer.println(json);
            return;
        }

        json = gson.toJson(exchange);
        writer.println(json);
    }


    private static class ErrorResponse {

        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }
}
