package com.capgemini.types;

import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApartmentSearchCriteria {
    private BigDecimal areaFrom;
    private BigDecimal areTo;
    private Integer roomQtyFrom;
    private Integer roomQtyTo;
    private Integer balconyQtyFrom;
    private Integer balconyQtyTo;
}
