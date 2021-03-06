package com.capgemini.dao.impl;

import com.capgemini.dao.ApartmentRepositoryCustom;
import com.capgemini.domain.ApartmentEntity;
import com.capgemini.domain.ClientEntity;
import com.capgemini.types.ApartmentSearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interface for apartment repository.
 */
public class ApartmentRepositoryImpl implements ApartmentRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Find apartments by main owner.
     * @param mainOwnerId Client id.
     * @return Apartments for given main owner.
     */
    @Override
    public List<ApartmentEntity> findApartmentsByMainOwner(long mainOwnerId) {
        ClientEntity mainOwner = entityManager.getReference(ClientEntity.class, mainOwnerId);
        TypedQuery<ApartmentEntity> query = entityManager.createQuery(
                "select a from ApartmentEntity a where :mainOwner = a.mainOwner", ApartmentEntity.class);
        query.setParameter("mainOwner", mainOwner);
        return query.getResultList();
    }

    /**
     * Find apartments by search criteria:
     * -room quantity,
     * -balcony quantity,
     * -area.
     * All criteria can be null.
     * @param criteria Object containing search criteria.
     * @return Apartments matched search criteria.
     */
    @Override
    public List<ApartmentEntity> findApartmentsByCriteria(ApartmentSearchCriteria criteria) {
        List<ApartmentEntity> emptyResult = new ArrayList<>();
        if (criteria == null) {
            return emptyResult;
        }
        boolean canAddEnd = false;
        Map<String, Object> params = new HashMap<>();
        StringBuilder queryString = new StringBuilder();
        queryString.append("select a from ApartmentEntity a where ");

        BigDecimal areaFrom = criteria.getAreaFrom();
        if (areaFrom != null) {
            canAddEnd = true;
            queryString.append("a.area >= :areaFrom");
            params.put("areaFrom", areaFrom);
        }

        BigDecimal areaTo = criteria.getAreaTo();
        if (areaTo != null) {
            if (canAddEnd) {
                queryString.append(" and ");
            }
            canAddEnd = true;
            queryString.append("a.area <= :areaTo");
            params.put("areaTo", areaTo);
        }

        Integer balconyQtyFrom = criteria.getBalconyQtyFrom();
        if (balconyQtyFrom != null) {
            if (canAddEnd) {
                queryString.append(" and ");
            }
            canAddEnd = true;
            queryString.append("a.balconyQty >= :balconyQtyFrom");
            params.put("balconyQtyFrom", balconyQtyFrom);
        }

        Integer balconyQtyTo = criteria.getBalconyQtyTo();
        if (balconyQtyTo != null) {
            if (canAddEnd) {
                queryString.append(" and ");
            }
            canAddEnd = true;
            queryString.append("a.balconyQty <= :balconyQtyTo");
            params.put("balconyQtyTo", balconyQtyTo);
        }

        Integer roomQtyFrom = criteria.getRoomQtyFrom();
        if (roomQtyFrom != null) {
            if (canAddEnd) {
                queryString.append(" and ");
            }
            canAddEnd = true;
            queryString.append("a.roomQty >= :roomQtyFrom");
            params.put("roomQtyFrom", roomQtyFrom);
        }

        Integer roomQtyTo = criteria.getRoomQtyTo();
        if (roomQtyTo != null) {
            if (canAddEnd) {
                queryString.append(" and ");
            }
            canAddEnd = true;
            queryString.append("a.roomQty <= :roomQtyTo");
            params.put("roomQtyTo", roomQtyTo);
        }

        queryString.append(" and upper(a.status) like upper('free')");

        if (canAddEnd) {
            TypedQuery<ApartmentEntity> query = entityManager.createQuery(queryString.toString(), ApartmentEntity.class);
            for (Map.Entry<String, Object> param : params.entrySet()) {
                query.setParameter(param.getKey(), param.getValue());
            }
            return query.getResultList();
        }
        return emptyResult;
    }

    /**
     * Find apartments with elevator in building or on ground floor.
     * @return Apartments with elevator in building or on ground floor.
     */
    @Override
    public List<ApartmentEntity> findApartmentsForDisabled() {
        TypedQuery<ApartmentEntity> query = entityManager.createQuery(
                "select a from ApartmentEntity a where (a.building.isElevatorPresent = true) or " +
                        "(a.floor = 0)", ApartmentEntity.class);
        return query.getResultList();
    }

    /**
     * Find reserved apartments for client.
     * @param clientId client id.
     * @return Reserved apartments for client.
     */
    @Override
    public List<ApartmentEntity> findReservedApartmentsForClient(Long clientId) {
        if (clientId == null) {
            return new ArrayList<>();
        }
        ClientEntity client = entityManager.getReference(ClientEntity.class, clientId);
        TypedQuery<ApartmentEntity> query = entityManager.createQuery(
                "select a from ApartmentEntity a where (a.mainOwner = :client) and " +
                        "(upper(a.status) like upper('reserved'))", ApartmentEntity.class);
        query.setParameter("client", client);
        return query.getResultList();
    }
}
