package org.dankts.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dankts.dto.CurrencyDto;
import org.dankts.exception.InvalidInputException;
import org.dankts.service.CurrencyService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private final CurrencyService currencyService = new CurrencyService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        PrintWriter printWriter = resp.getWriter();
        String code = pathInfo.substring(1).toUpperCase();
        if (code.length() != 3) {
            throw new InvalidInputException();
        }
        CurrencyDto currencyByCode = currencyService.getCurrencyByCode(code);
        checkValidGetRequest(resp, currencyByCode, code, printWriter);
    }

    private void checkValidGetRequest(HttpServletResponse resp, CurrencyDto currencyByCode, String code, PrintWriter printWriter) {
        if (currencyByCode != null && code.length() == 3) {
            String json = gson.toJson(currencyByCode);
            printWriter.println(json);
        } else if (code.length() > 3) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
