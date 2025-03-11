package org.dankts.dao;

import org.dankts.entity.Currency;
import java.util.List;

public interface CurrencyDao {

    List<Currency> findAll();

    Currency save(Currency entity);

    Currency getByCode(String code);

}
