package org.dankts.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CurrencyDto {

    private Integer id;
    private String name;
    private String code;
    private String sign;

}
