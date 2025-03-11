package org.dankts.dao;

import org.dankts.entity.ExchangeRate;

import java.util.List;

public interface ExchangeRatesDao {

    ExchangeRate save(ExchangeRate entity);

    List<ExchangeRate> findAll();

    ExchangeRate findByCode(String baseCurrencyCode, String targetCurrencyCode);

    ExchangeRate update(ExchangeRate entity);
}
