package com.capgemini.types;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BuildingTO {
    Long id;
    private String description;
    private AddressTO address;
    private Integer floorQty;
    private Boolean isElevatorPresent;
    private Integer apartmentsQty;
}
