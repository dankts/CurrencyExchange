package org.dankts.service;

import org.dankts.dao.ExchangeRatesDao;
import org.dankts.dao.impl.ExchangeRatesDaoImpl;
import org.dankts.dto.ExchangeDto;
import org.dankts.entity.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangeService {

    private final ExchangeRatesDao exchangeRatesDao = new ExchangeRatesDaoImpl();

    public ExchangeDto exchange(String from, String to, BigDecimal amount) {
        Optional<ExchangeRate> byCode = exchangeRatesDao.findByCode(from, to);
        if (byCode.isPresent()) {
            ExchangeRate pair = byCode.get();
            return ExchangeDto.builder()
                    .id(pair.getId())
                    .baseCurrency(pair.getBaseCurrencyId())
                    .targetCurrency(pair.getTargetCurrencyId())
                    .rate(pair.getRate())
                    .amount(amount)
                    .convertedAmount(amount.multiply(pair.getRate()))
                    .build();
        }

        byCode = exchangeRatesDao.findByCode(to, from);
        if (byCode.isPresent()) {
            ExchangeRate pair = byCode.get();
            BigDecimal rate = BigDecimal.ONE.divide(pair.getRate(), 2, RoundingMode.HALF_UP);
            return ExchangeDto.builder()
                    .id(pair.getId())
                    .baseCurrency(pair.getTargetCurrencyId())
                    .targetCurrency(pair.getBaseCurrencyId())
                    .rate(rate)
                    .amount(amount)
                    .convertedAmount(rate.multiply(amount))
                    .build();
        }
        return null;
    }
}
