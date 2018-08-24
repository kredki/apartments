package com.capgemini.testutils;

import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ClientEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientGenerator {
    public ClientEntity getClient() {
        AddressInTable address = AddressInTable.builder()
                .city("city")
                .no("no")
                .postalCode("code")
                .street("street")
                .build();
        return ClientEntity.builder()
                .telephone("telephone")
                .lastName("lastname")
                .firstName("firstname")
                .address(address)
                .build();
    }
}
