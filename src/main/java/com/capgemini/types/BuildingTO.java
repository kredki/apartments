package com.capgemini.types;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
public class BuildingTO {
    private Long version;
    private Long id;
    private String description;
    private AddressTO address;
    private Integer floorQty;
    private Boolean isElevatorPresent;
    private Integer apartmentsQty;
    Set<Long> apartments = new HashSet<>();
}
