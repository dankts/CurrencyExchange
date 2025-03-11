package org.dankts.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.dankts.exception.CurrencyCodeExistsException;
import org.dankts.exception.CurrencyNotFoundException;
import org.dankts.exception.CurrencyPairAlreadyExistsException;
import org.dankts.exception.InvalidInputException;

import java.io.IOException;
import java.sql.SQLException;

    @WebFilter("/*")
    public class ErrorsFilter implements Filter {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            try {
                chain.doFilter(request, response);
            } catch (Exception e) {
                httpServletResponse.sendError(handleException(e));
            }
        }

        private int handleException(Exception e) {
            if (e instanceof SQLException) {
                return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            } else if (e instanceof CurrencyNotFoundException) {
                return HttpServletResponse.SC_NOT_FOUND;
            } else if (e instanceof InvalidInputException) {
                return HttpServletResponse.SC_BAD_REQUEST;
            } else if (e instanceof CurrencyCodeExistsException || e instanceof CurrencyPairAlreadyExistsException) {
                return HttpServletResponse.SC_CONFLICT;
            }
            return HttpServletResponse.SC_BAD_GATEWAY;
        }
    }
