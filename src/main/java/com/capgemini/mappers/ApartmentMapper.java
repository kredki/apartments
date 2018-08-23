package com.capgemini.mappers;

import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ApartmentEntity;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.domain.ClientEntity;
import com.capgemini.types.AddressTO;
import com.capgemini.types.ApartmentTO;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for apartment
 */
@Component
public class ApartmentMapper {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Map entity to TO.
     * @param apartment Object to map.
     * @return Mapped object.
     */
    public ApartmentTO toTO(ApartmentEntity apartment) {
        if (apartment == null) {
            return null;
        }
        AddressTO address = AddressMapper.toTO(apartment.getAddress());
        Long buildingId = apartment.getBuilding().getId();
        Set<Long> ownersIds = new HashSet<>();
        Set<ClientEntity> owners = apartment.getOwners();
        for(ClientEntity owner : owners) {
            ownersIds.add(owner.getId());
        }

        return ApartmentTO.builder().address(address).area(apartment.getArea()).balconyQty(apartment.getBalconyQty())
                .building(buildingId).floor(apartment.getFloor()).id(apartment.getId()).owners(ownersIds).price(apartment.getPrice())
                .roomQty(apartment.getRoomQty()).status(apartment.getStatus()).version(apartment.getVersion()).build();
    }

    /**
     * Map TO to entity.
     * @param apartment Object to map.
     * @return Mapped object.
     */
    public ApartmentEntity toEntity(ApartmentTO apartment) {
        if (apartment == null) {
            return null;
        }
        AddressInTable address = AddressMapper.toInTable(apartment.getAddress());

        BuildingEntity building = entityManager.getReference(BuildingEntity.class, apartment.getBuilding());
        Set<Long> ownersIds = apartment.getOwners();
        Set<ClientEntity> owners = new HashSet<>();
        for (Long id : ownersIds) {
            owners.add(entityManager.getReference(ClientEntity.class, id));
        }
        return ApartmentEntity.builder().address(address).area(apartment.getArea()).balconyQty(apartment.getBalconyQty())
                .building(building).floor(apartment.getFloor()).id(apartment.getId()).owners(owners).price(apartment.getPrice())
                .roomQty(apartment.getRoomQty()).status(apartment.getStatus()).version(apartment.getVersion()).build();
    }

    /**
     * Map set of entities to set of TOs.
     * @param apartments Objects to map.
     * @return Mapped objects.
     */
    public Set<ApartmentTO> map2TOs (Set<ApartmentEntity> apartments) {
        return apartments.stream().map(this::toTO).collect(Collectors.toSet());
    }

    /**
     * Map set of TOs to set of entities.
     * @param apartments Objects to map.
     * @return Mapped objects.
     */
    public Set<ApartmentEntity> map2Entities (Set<ApartmentTO> apartments) {
        return apartments.stream().map(this::toEntity).collect(Collectors.toSet());
    }

    /**
     * Map set of entities to set of TOs.
     * @param apartments Objects to map.
     * @return Mapped objects.
     */
    public List<ApartmentTO> map2TOs (List<ApartmentEntity> apartments) {
        return apartments.stream().map(this::toTO).collect(Collectors.toList());
    }

    /**
     * Map set of TOs to set of entities.
     * @param apartments Objects to map.
     * @return Mapped objects.
     */
    public List<ApartmentEntity> map2Entities (List<ApartmentTO> apartments) {
        return apartments.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
