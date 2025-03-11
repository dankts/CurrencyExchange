package org.dankts.dto;

import lombok.Builder;
import lombok.Data;
import org.dankts.entity.Currency;

import java.math.BigDecimal;

@Data
@Builder
public class ExchangeDto {

    private Integer id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;
}
