package org.dankts.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dankts.dto.CreateExchangeRateDto;
import org.dankts.dto.ExchangeRateDto;
import org.dankts.exception.InvalidInputException;
import org.dankts.service.ExchangeRateService;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private final Gson gson = new Gson();
    private final ExchangeRateService exchangeRateService = new ExchangeRateService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        List<ExchangeRateDto> exchangeRates = exchangeRateService.getExchangeRates();
        String json = gson.toJson(exchangeRates);
        writer.println(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String paramRate = req.getParameter("rate");
        BigDecimal rate;
        if (baseCurrencyCode == null || targetCurrencyCode == null || paramRate == null) {
            throw new InvalidInputException();
        } else {
            rate = new BigDecimal(paramRate);
        }
        PrintWriter writer = resp.getWriter();
        ExchangeRateDto exchangeRateDto = exchangeRateService.create(CreateExchangeRateDto.builder()
                .baseCurrency(baseCurrencyCode)
                .targetCurrency(targetCurrencyCode)
                .rate(rate)
                .build());
        String json = gson.toJson(exchangeRateDto);
        writer.println(json);
    }
}
