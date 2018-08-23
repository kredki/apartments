package com.capgemini.dao.impl;

import com.capgemini.dao.ClientRepositoryCustom;
import com.capgemini.domain.ClientEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;

/**
 * Custom interface implementation for client repository.
 */
public class ClientRepositoryImpl implements ClientRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Find sum of price of apartments for given client.
     * @param clientId Client id.
     * @return Sum of price of apartments for given client.
     */
    @Override
    public BigDecimal findApartmentsWorthForClient(Long clientId) {
        ClientEntity client = entityManager.getReference(ClientEntity.class, clientId);
        TypedQuery<BigDecimal> query = entityManager.createQuery(
                "select sum(a.price) from ApartmentEntity a where :client member of a.owners", BigDecimal.class);
        query.setParameter("client", client);
        return query.getSingleResult();
    }

    /**
     * Find clients with more than one apartment.
     * @return Clients with more than one apartment.
     */
    @Override
    public List<ClientEntity> findClientsWithMoreThanOneApartment() {
        TypedQuery<ClientEntity> query = entityManager.createQuery(
                "select c from ClientEntity c where c.id in " +
                        "(select c2.id from ClientEntity c2 where count(c.apartments) > 1", ClientEntity.class);
        return query.getResultList();
    }
}
