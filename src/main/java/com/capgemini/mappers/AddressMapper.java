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

        return new AddressTO.Builder().withCity(address.getCity()).withNo(address.getNo())
                .withPostalCode(address.getPostalCode()).withStreet(address.getStreet()).build();
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

        return new AddressInTable.Builder().withCity(address.getCity()).withNo(address.getNo())
                .withPostalCode(address.getPostalCode()).withStreet(address.getStreet()).build();
    }
}
