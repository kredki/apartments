package com.capgemini.mappers;

import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ApartmentEntity;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.types.AddressTO;
import com.capgemini.types.BuildingTO;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for building.
 */
@Component
public class BuildingMapper {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Map entity to TO.
     * @param building Object to map.
     * @return Mapped object.
     */
    public BuildingTO toTO(BuildingEntity building) {
        if (building == null) {
            return null;
        }
        AddressTO address = AddressMapper.toTO(building.getAddress());
        Set<Long> apartmentsIds = new HashSet<>();
        Set<ApartmentEntity> apartments = building.getApartments();
        for (ApartmentEntity apartment : apartments) {
            apartmentsIds.add(apartment.getId());
        }

        return BuildingTO.builder().address(address).apartmentsQty(building.getApartmentsQty())
                .description(building.getDescription()).floorQty(building.getFloorQty()).id(building.getId())
                .isElevatorPresent(building.getIsElevatorPresent()).version(building.getVersion())
                .apartments(apartmentsIds).build();
    }

    /**
     * Map TO to entity.
     * @param building Object to map.
     * @return Mapped object.
     */
    public BuildingEntity toEntity(BuildingTO building) {
        if (building == null) {
            return null;
        }
        AddressInTable address = AddressMapper.toInTable(building.getAddress());
        Set<Long> apartmentsIds = building.getApartments();
        Set<ApartmentEntity> apartments = new HashSet<>();
        for (Long id : apartmentsIds) {
            apartments.add(entityManager.getReference(ApartmentEntity.class, id));
        }

        return BuildingEntity.builder().address(address).apartmentsQty(building.getApartmentsQty())
                .description(building.getDescription()).floorQty(building.getFloorQty()).id(building.getId())
                .isElevatorPresent(building.getIsElevatorPresent()).version(building.getVersion()).apartments(apartments)
                .build();
    }

    /**
     * Map set of entities to set of TOs.
     * @param buildings Objects to map.
     * @return Mapped objects.
     */
    public Set<BuildingTO> map2TOs (Set<BuildingEntity> buildings) {
        return buildings.stream().map(this::toTO).collect(Collectors.toSet());
    }

    /**
     * Map set of TOs to set of entities.
     * @param buildings Objects to map.
     * @return Mapped objects.
     */
    public Set<BuildingEntity> map2Entities (Set<BuildingTO> buildings) {
        return buildings.stream().map(this::toEntity).collect(Collectors.toSet());
    }
}
