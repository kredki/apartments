package com.capgemini.mappers;

import com.capgemini.domain.AddressInTable;
import com.capgemini.types.AddressTO;

/**
 * Mapper for address
 */
public class AddressMapper {
    /**
     * Map AddressInTable to TO.
     * @param address Object to map.
     * @return Mapped object.
     */
    public static AddressTO toTO(AddressInTable address) {
        if (address == null) {
            return null;
        }

        return AddressTO.builder().city(address.getCity()).no(address.getNo()).postalCode(address.getPostalCode())
                .street(address.getStreet()).build();
    }

    /**
     * Map TO to AddressInTable.
     * @param address Object to map.
     * @return Mapped object.
     */
    public static AddressInTable toInTable(AddressTO address) {
        if (address == null) {
            return null;
        }

        return AddressInTable.builder().city(address.getCity()).no(address.getNo()).postalCode(address.getPostalCode())
                .street(address.getStreet()).build();
    }
}
