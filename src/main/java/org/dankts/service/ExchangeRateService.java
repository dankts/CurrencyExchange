package org.dankts.service;

import org.dankts.dao.CurrencyDao;
import org.dankts.dao.ExchangeRatesDao;
import org.dankts.dao.impl.CurrencyDaoImpl;
import org.dankts.dao.impl.ExchangeRatesDaoImpl;
import org.dankts.dto.CreateExchangeRateDto;
import org.dankts.dto.ExchangeRateDto;
import org.dankts.entity.Currency;
import org.dankts.entity.ExchangeRate;
import org.dankts.exception.CurrencyNotFoundException;
import org.dankts.exception.CurrencyPairAlreadyExistsException;
import org.dankts.mapper.ExchangeRateMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ExchangeRateService {

    private final ExchangeRatesDao exchangeRatesDao = new ExchangeRatesDaoImpl();
    private final ExchangeRateMapper exchangeRateMapper = ExchangeRateMapper.INSTANCE;
    private final CurrencyDao currencyDao = new CurrencyDaoImpl();

    public ExchangeRateDto create(CreateExchangeRateDto createExchangeRateDto) {
        Currency baseCurrencyId = currencyDao.getByCode(createExchangeRateDto.getBaseCurrency());
        Currency targetCurrencyId = currencyDao.getByCode(createExchangeRateDto.getTargetCurrency());
        if (baseCurrencyId == null || targetCurrencyId == null) {
            throw new CurrencyNotFoundException();
        } else if (exchangeRatesDao.findByCode(createExchangeRateDto.getBaseCurrency(),
                createExchangeRateDto.getTargetCurrency()) != null) {
            throw new CurrencyPairAlreadyExistsException();
        }
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBaseCurrencyId(baseCurrencyId);
        exchangeRate.setTargetCurrencyId(targetCurrencyId);
        exchangeRate.setRate(createExchangeRateDto.getRate());

        exchangeRatesDao.save(exchangeRate);

        return exchangeRateMapper.toDto(exchangeRate);
    }

    public List<ExchangeRateDto> getExchangeRates() {
        return exchangeRatesDao.findAll().stream()
                .map(exchangeRateMapper::toDto)
                .collect(Collectors.toList());
    }

    public ExchangeRateDto getExchangeRateByCode(String baseCurrencyCode, String targetCurrencyCode) {
        ExchangeRate exchangeRate = exchangeRatesDao.findByCode(baseCurrencyCode, targetCurrencyCode);
        if (exchangeRate == null) {
            throw new CurrencyNotFoundException();
        }
        return exchangeRateMapper.toDto(exchangeRate);
    }

    public ExchangeRateDto updateExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        Currency baseCurrency = currencyDao.getByCode(baseCurrencyCode);
        Currency targetCurrency = currencyDao.getByCode(targetCurrencyCode);
        ExchangeRate exchangeRate = exchangeRatesDao.findByCode(baseCurrency.getCode(), targetCurrency.getCode());

        if (exchangeRate == null) {
            throw new CurrencyNotFoundException();
        }

        ExchangeRate exchangeRate1 = new ExchangeRate(exchangeRate.getId(),
                baseCurrency,
                targetCurrency,
                rate);
        ExchangeRate update = exchangeRatesDao.update(exchangeRate1);
        return exchangeRateMapper.toDto(update);
    }
}
