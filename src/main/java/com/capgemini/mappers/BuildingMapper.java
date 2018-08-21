package com.capgemini.mappers;

import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.types.AddressTO;
import com.capgemini.types.BuildingTO;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for building.
 */
public class BuildingMapper {
    /**
     * Map entity to TO.
     * @param building Object to map.
     * @return Mapped object.
     */
    public static BuildingTO toTO(BuildingEntity building) {
        if (building == null) {
            return null;
        }
        AddressTO address = AddressMapper.toTO(building.getAddress());

        return BuildingTO.builder().address(address).apartmentsQty(building.getApartmentsQty())
                .description(building.getDescription()).floorQty(building.getFloorQty()).id(building.getId())
                .isElevatorPresent(building.getIsElevatorPresent()).build();
    }

    /**
     * Map TO to entity.
     * @param building Object to map.
     * @return Mapped object.
     */
    public static BuildingEntity toEntity(BuildingTO building) {
        if (building == null) {
            return null;
        }
        AddressInTable address = AddressMapper.toInTable(building.getAddress());

        return BuildingEntity.builder().address(address).apartmentsQty(building.getApartmentsQty())
                .description(building.getDescription()).floorQty(building.getFloorQty()).id(building.getId())
                .isElevatorPresent(building.getIsElevatorPresent()).build();
    }

    /**
     * Map set of entities to set of TOs.
     * @param buildings Objects to map.
     * @return Mapped objects.
     */
    public static Set<BuildingTO> map2TOs (Set<BuildingEntity> buildings) {
        return buildings.stream().map(BuildingMapper::toTO).collect(Collectors.toSet());
    }

    /**
     * Map set of TOs to set of entities.
     * @param buildings Objects to map.
     * @return Mapped objects.
     */
    public static Set<BuildingEntity> map2Entities (Set<BuildingTO> buildings) {
        return buildings.stream().map(BuildingMapper::toEntity).collect(Collectors.toSet());
    }
}
