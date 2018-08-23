package com.capgemini.dao;

import com.capgemini.domain.ClientEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Custom interface for client repository.
 */
public interface ClientRepositoryCustom {
    /**
     * Find sum of price of apartments for given client.
     * @param clientId Client id.
     * @return Sum of price of apartments for given client.
     */
    BigDecimal findApartmentsWorthForClient(Long clientId);

    /**
     * Find clients with more than one apartment.
     * @return Clients with more than one apartment.
     */
    List<ClientEntity> findClientsWithMoreThanOneApartment();
}
