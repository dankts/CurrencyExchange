package org.dankts.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CreateExchangeRateDto {

    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal rate;
}
