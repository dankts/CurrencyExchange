package org.dankts.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {

    private Integer id;
    private Currency baseCurrencyId;
    private Currency targetCurrencyId;
    private BigDecimal rate;
}