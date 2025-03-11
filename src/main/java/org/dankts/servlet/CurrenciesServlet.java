package org.dankts.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dankts.dto.CreateCurrencyDto;
import org.dankts.dto.CurrencyDto;
import org.dankts.exception.InvalidInputException;
import org.dankts.service.CurrencyService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final CurrencyService currencyService = new CurrencyService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        List<CurrencyDto> currencies = currencyService.getCurrencies();
        String json = gson.toJson(currencies);
        writer.println(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        if (name == null || code == null || sign == null) {
            throw new InvalidInputException();
        }

        CurrencyDto currencyDto = currencyService.create(CreateCurrencyDto.builder()
                .name(name)
                .code(code)
                .sign(sign)
                .build());
        PrintWriter printWriter = resp.getWriter();
        String json = gson.toJson(currencyDto);
        printWriter.println(json);
    }
}
