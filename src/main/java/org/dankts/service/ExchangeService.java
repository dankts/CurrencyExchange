package org.dankts.service;

import org.dankts.dao.ExchangeRatesDao;
import org.dankts.dao.impl.ExchangeRatesDaoImpl;
import org.dankts.dto.ExchangeDto;
import org.dankts.entity.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeService {

    private final ExchangeRatesDao exchangeRatesDao = new ExchangeRatesDaoImpl();

    public ExchangeDto exchange(String from, String to, BigDecimal amount) {
        ExchangeRate byCode;
        byCode = exchangeRatesDao.findByCode(from, to);
        if (byCode != null) {
            return ExchangeDto.builder()
                    .id(byCode.getId())
                    .baseCurrency(byCode.getBaseCurrencyId())
                    .targetCurrency(byCode.getTargetCurrencyId())
                    .rate(byCode.getRate())
                    .amount(amount)
                    .convertedAmount(amount.multiply(byCode.getRate()))
                    .build();
        }

        byCode = exchangeRatesDao.findByCode(to, from);

        if (byCode != null) {
            BigDecimal rate = BigDecimal.ONE.divide(byCode.getRate(), 2, RoundingMode.HALF_UP);
            return ExchangeDto.builder()
                    .id(byCode.getId())
                    .baseCurrency(byCode.getTargetCurrencyId())
                    .targetCurrency(byCode.getBaseCurrencyId())
                    .rate(rate)
                    .amount(amount)
                    .convertedAmount(rate.multiply(amount))
                    .build();
        }
        return null;
    }


}
