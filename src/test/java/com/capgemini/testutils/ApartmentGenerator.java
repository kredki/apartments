package com.capgemini.testutils;

import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ApartmentEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ApartmentGenerator {
    public ApartmentEntity getFreeApartment() {
        AddressInTable address= AddressInTable.builder()
                .city("city")
                .no("no")
                .postalCode("postal code")
                .street("street")
                .build();

        return ApartmentEntity.builder()
                .balconyQty(1)
                .floor(0)
                .status("sold")
                .price(new BigDecimal("50000.00"))
                .roomQty(3)
                .area(new BigDecimal(100.00))
                .address(address)
                .build();
    }
}
