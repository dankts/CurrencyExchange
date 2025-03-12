package org.dankts.dao;

import org.dankts.entity.Currency;
import java.util.List;
import java.util.Optional;

public interface CurrencyDao {

    List<Currency> findAll();

    Currency save(Currency entity);

    Optional<Currency> getByCode(String code);

}
