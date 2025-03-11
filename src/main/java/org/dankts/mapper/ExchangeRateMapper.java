package org.dankts.mapper;

import org.dankts.dto.ExchangeRateDto;
import org.dankts.entity.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExchangeRateMapper {

    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    @Mapping(source = "baseCurrencyId", target = "baseCurrency")
    @Mapping(source = "targetCurrencyId", target = "targetCurrency")
    ExchangeRateDto toDto(ExchangeRate entity);
}
