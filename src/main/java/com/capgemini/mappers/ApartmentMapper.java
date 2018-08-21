package com.capgemini.mappers;

import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ApartmentEntity;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.domain.ClientEntity;
import com.capgemini.types.AddressTO;
import com.capgemini.types.ApartmentTO;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ApartmentMapper {
    /**
     * Map entity to TO.
     * @param apartment Object to map.
     * @return Mapped object.
     */
    public static ApartmentTO toTO(ApartmentEntity apartment) {
        if (apartment == null) {
            return null;
        }
        AddressTO address = AddressMapper.toTO(apartment.getAddress());
        Long buildingId = 0L;
        Set<Long> ownersIds = new HashSet<>();

        return ApartmentTO.builder().address(address).area(apartment.getArea()).balconyQty(apartment.getBalconyQty())
                .building(buildingId).floor(apartment.getFloor()).id(apartment.getId()).owners(ownersIds).price(apartment.getPrice())
                .roomQty(apartment.getRoomQty()).status(apartment.getStatus()).build();
    }

    /**
     * Map TO to entity.
     * @param apartment Object to map.
     * @return Mapped object.
     */
    public static ApartmentEntity toEntity(ApartmentTO apartment) {
        if (apartment == null) {
            return null;
        }
        AddressInTable address = AddressMapper.toInTable(apartment.getAddress());

        BuildingEntity building = null;
        Set<ClientEntity> owners = new HashSet<>();
        return ApartmentEntity.builder().address(address).area(apartment.getArea()).balconyQty(apartment.getBalconyQty())
                .building(building).floor(apartment.getFloor()).id(apartment.getId()).owners(owners).price(apartment.getPrice())
                .roomQty(apartment.getRoomQty()).status(apartment.getStatus()).build();
    }

    /**
     * Map set of entities to set of TOs.
     * @param apartments Objects to map.
     * @return Mapped objects.
     */
    public static Set<ApartmentTO> map2TOs (Set<ApartmentEntity> apartments) {
        return apartments.stream().map(ApartmentMapper::toTO).collect(Collectors.toSet());
    }

    /**
     * Map set of TOs to set of entities.
     * @param apartments Objects to map.
     * @return Mapped objects.
     */
    public static Set<ApartmentEntity> map2Entities (Set<ApartmentTO> apartments) {
        return apartments.stream().map(ApartmentMapper::toEntity).collect(Collectors.toSet());
    }
}
