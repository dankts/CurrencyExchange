package org.dankts.entity;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Currency {

    private Integer id;
    private String code;
    private String fullName;
    private String sign;

    public Currency(Integer id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }
}
