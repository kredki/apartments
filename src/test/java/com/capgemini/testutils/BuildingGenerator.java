package com.capgemini.testutils;

import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.BuildingEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class BuildingGenerator {
    public BuildingEntity getBuilding() {
        AddressInTable addressInTable = AddressInTable.builder()
                .street("street")
                .postalCode("code")
                .no("no").city("city")
                .build();
        return BuildingEntity.builder()
                .address(addressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
    }
}
