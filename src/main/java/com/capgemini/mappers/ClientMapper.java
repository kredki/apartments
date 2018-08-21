package com.capgemini.mappers;

import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ClientEntity;
import com.capgemini.types.AddressTO;
import com.capgemini.types.ClientTO;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for client
 */
public class ClientMapper {
    /**
     * Map entity to TO.
     * @param client Object to map.
     * @return Mapped object.
     */
    public static ClientTO toTO(ClientEntity client) {
        if (client == null) {
            return null;
        }
        AddressTO address = AddressMapper.toTO(client.getAddress());

        return new ClientTO.Builder().withFirstName(client.getFirstName()).withId(client.getId())
                .withLastName(client.getLastName()).withTelephone(client.getTelephone()).withAddress(address)
                .build();
    }

    /**
     * Map TO to entity.
     * @param client Object to map.
     * @return Mapped object.
     */
    public static ClientEntity toEntity(ClientTO client) {
        if (client == null) {
            return null;
        }
        AddressInTable address = AddressMapper.toInTable(client.getAddress());

        return new ClientEntity.Builder().withFirstName(client.getFirstName()).withId(client.getId())
                .withLastName(client.getLastName()).withTelephone(client.getTelephone()).withAddress(address)
                .build();
    }

    /**
     * Map set of entities to set of TOs.
     * @param clients Objects to map.
     * @return Mapped objects.
     */
    public static Set<ClientTO> map2TOs (Set<ClientEntity> clients) {
        return clients.stream().map(ClientMapper::toTO).collect(Collectors.toSet());
    }

    /**
     * Map set of TOs to set of entities.
     * @param clients Objects to map.
     * @return Mapped objects.
     */
    public static Set<ClientEntity> map2Entities (Set<ClientTO> clients) {
        return clients.stream().map(ClientMapper::toEntity).collect(Collectors.toSet());
    }
}
