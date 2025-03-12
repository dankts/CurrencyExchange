package org.dankts.dao;

import org.dankts.entity.ExchangeRate;

import java.util.List;
import java.util.Optional;

public interface ExchangeRatesDao {

    ExchangeRate save(ExchangeRate entity);

    List<ExchangeRate> findAll();

    Optional<ExchangeRate> findByCode(String baseCurrencyCode, String targetCurrencyCode);

    ExchangeRate update(ExchangeRate entity);
}
