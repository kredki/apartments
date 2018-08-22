package com.capgemini.types;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
public class ApartmentTO {
    private Long version;
    private Long id;
    private BigDecimal area;
    private Integer roomQty;
    private Integer balconyQty;
    private Integer floor;
    private AddressTO address;
    private String status;
    private BigDecimal price;
    private Set<Long> owners = new HashSet<>();
    private Long building;
}
