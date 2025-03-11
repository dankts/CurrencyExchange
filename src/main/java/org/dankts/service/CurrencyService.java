package org.dankts.service;

import org.dankts.dao.CurrencyDao;
import org.dankts.dao.impl.CurrencyDaoImpl;
import org.dankts.dto.CreateCurrencyDto;
import org.dankts.dto.CurrencyDto;
import org.dankts.entity.Currency;
import org.dankts.exception.CurrencyCodeExistsException;
import org.dankts.exception.CurrencyNotFoundException;
import org.dankts.mapper.CurrencyMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CurrencyService {

    private final CurrencyMapper currencyMapper = CurrencyMapper.INSTANCE;
    private final CurrencyDao currencyDao = new CurrencyDaoImpl();

    public List<CurrencyDto> getCurrencies() {
        return currencyDao.findAll()
                .stream().map(currencyMapper::toDto)
                .collect(Collectors.toList());
    }

    public CurrencyDto create(CreateCurrencyDto entity) {
        Currency currency = currencyMapper.toEntity(entity);
        if (currencyDao.getByCode(currency.getCode()).getCode().equals(entity.getCode())) {
            throw new CurrencyCodeExistsException();
        }
        Currency saveCurrency = currencyDao.save(currency);
        return currencyMapper.toDto(saveCurrency);
    }

    public CurrencyDto getCurrencyByCode(String code) {
        Currency byCode = currencyDao.getByCode(code);
        if (byCode == null) {
            throw new CurrencyNotFoundException();
        }
        return currencyMapper.toDto(currencyDao.getByCode(code));
    }
}
