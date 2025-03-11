package org.dankts.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dankts.dto.ExchangeRateDto;
import org.dankts.service.ExchangeRateService;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.InputMismatchException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private final Gson gson = new Gson();
    private final ExchangeRateService exchangeRateService = new ExchangeRateService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String currencyPair = pathInfo.substring(1);
        if (currencyPair.length() != 6) {
            throw new InputMismatchException();
        }
        String baseCurrencyCode = currencyPair.substring(0, 3);
        String targetCurrencyCode = currencyPair.substring(3, 6);
        PrintWriter writer = resp.getWriter();
        ExchangeRateDto exchangeRateByCode = exchangeRateService
                .getExchangeRateByCode(baseCurrencyCode, targetCurrencyCode);
        String json = gson.toJson(exchangeRateByCode);
        writer.println(json);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String baseCurrencyCode = pathInfo.substring(1, 4);
        String targetCurrencyCode = pathInfo.substring(4);
        String rateParam = req.getParameter("rate");
        if (rateParam == null) {
            throw new InputMismatchException();
        }
        BigDecimal rate = null;
        if (!rateParam.isEmpty()) {
            rate = new BigDecimal(rateParam);
        }
        PrintWriter writer = resp.getWriter();
        ExchangeRateDto exchangeRateDto = exchangeRateService.updateExchangeRate(baseCurrencyCode,
                targetCurrencyCode,
                rate);
        String json = gson.toJson(exchangeRateDto);
        writer.println(json);
    }
}
