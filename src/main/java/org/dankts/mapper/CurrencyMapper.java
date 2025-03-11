package org.dankts.mapper;

import org.dankts.dto.CreateCurrencyDto;
import org.dankts.dto.CurrencyDto;
import org.dankts.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrencyMapper {

    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    @Mapping(source = "name", target = "fullName")
    Currency toEntity(CreateCurrencyDto CreateCurrencyDto);

    @Mapping(source = "fullName", target = "name")
    CurrencyDto toDto(Currency currency);
}
