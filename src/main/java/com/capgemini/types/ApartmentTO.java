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
    Long id;
    BigDecimal area;
    Integer roomQty;
    Integer balconyQty;
    Integer floor;
    AddressTO address;
    String status;
    BigDecimal price;
    Set<Long> owners = new HashSet<>();
    Long building;
}
