package com.capgemini.mappers;

import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ApartmentEntity;
import com.capgemini.domain.ClientEntity;
import com.capgemini.types.AddressTO;
import com.capgemini.types.ClientTO;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for client
 */
@Component
public class ClientMapper {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Map entity to TO.
     * @param client Object to map.
     * @return Mapped object.
     */
    public ClientTO toTO(ClientEntity client) {
        if (client == null) {
            return null;
        }
        AddressTO address = AddressMapper.toTO(client.getAddress());
        Set<ApartmentEntity> apartments = client.getApartments();
        Set<Long> apartmentsIds = new HashSet<>();
        for (ApartmentEntity apartment : apartments) {
            apartmentsIds.add(apartment.getId());
        }

        return ClientTO.builder().firstName(client.getFirstName()).id(client.getId()).lastName(client.getLastName())
                .telephone(client.getTelephone()).address(address).version(client.getVersion()).apartments(apartmentsIds)
                .build();
    }

    /**
     * Map TO to entity.
     * @param client Object to map.
     * @return Mapped object.
     */
    public ClientEntity toEntity(ClientTO client) {
        if (client == null) {
            return null;
        }
        AddressInTable address = AddressMapper.toInTable(client.getAddress());
        Set<ApartmentEntity> apartments = new HashSet<>();
        Set<Long> apartmentsIds = client.getApartments();
        for (Long id : apartmentsIds) {
            apartments.add(entityManager.getReference(ApartmentEntity.class, id));
        }

        return ClientEntity.builder().firstName(client.getFirstName()).id(client.getId()).lastName(client.getLastName())
                .telephone(client.getTelephone()).address(address).version(client.getVersion()).apartments(apartments)
                .build();
    }

    /**
     * Map set of entities to set of TOs.
     * @param clients Objects to map.
     * @return Mapped objects.
     */
    public Set<ClientTO> map2TOs (Set<ClientEntity> clients) {
        return clients.stream().map(this::toTO).collect(Collectors.toSet());
    }

    /**
     * Map set of TOs to set of entities.
     * @param clients Objects to map.
     * @return Mapped objects.
     */
    public Set<ClientEntity> map2Entities (Set<ClientTO> clients) {
        return clients.stream().map(this::toEntity).collect(Collectors.toSet());
    }
}
