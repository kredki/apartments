package com.capgemini.dao.impl;

import com.capgemini.dao.BuildingRepositoryCustom;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.domain.QApartmentEntity;
import com.capgemini.domain.QBuildingEntity;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;

/**
 * Custom interface implementation for building repository.
 */
public class BuildingRepositoryImpl implements BuildingRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Find average price in given building.
     * @param buildingId Building id.
     * @return Average price in given building.
     */
    @Override
    public BigDecimal findAvgPriceForBuilding(Long buildingId) {
        BuildingEntity building = entityManager.getReference(BuildingEntity.class, buildingId);
        TypedQuery<Double> query = entityManager.createQuery(
                "select avg(a.price) from ApartmentEntity a where :building = a.building", Double.class);
        query.setParameter("building", building);
        Double result = query.getSingleResult();
        return new BigDecimal(result);
    }

    /**
     * Return number of apartments of given status in given building.
     * @param buildingId Building id.
     * @param status Status of apartment.
     * @return Number of apartments of given status in given building.
     */

    @Override
    public Long countByStatus(Long buildingId, String status) {
        BuildingEntity building = entityManager.getReference(BuildingEntity.class, buildingId);
        TypedQuery<Long> query = entityManager.createQuery(
                "select count(a) from ApartmentEntity a where :building = a.building and :status = status",
                Long.class);
        query.setParameter("building", building);
        query.setParameter("status", status);
        return query.getSingleResult();
    }

    /**
     * Find building with most number of free apartments.
     * @return Building with most number of free apartments.
     */
    @Override
    public List<BuildingEntity> findBuildingWithMostFreeApartments() {
        JPAQuery<BuildingEntity> query = new JPAQuery(entityManager);
        JPAQuery<Tuple> query2 = new JPAQuery(entityManager);
        JPAQuery<Long> query3 = new JPAQuery(entityManager);
        QBuildingEntity building = QBuildingEntity.buildingEntity;
        QApartmentEntity apartment = QApartmentEntity.apartmentEntity;
        query2 = query2.select(apartment.count(), apartment.building.id).from(apartment).where(apartment.status.lower().eq("free"))
                .groupBy(apartment.building);

    List<BuildingEntity> result = query.select(building).from(building)
                .where(building.in(
                        JPAExpressions.select(apartment.building)
                .where(apartment.in(
                        JPAExpressions.select(apartment)
                        .where(apartment.status.lower().eq("free"))
                        .groupBy(apartment.building)
                        .having(apartment.count().eq(
                                JPAExpressions.select(query2.select(apartment.count().max()))
                        ))
                )))
        ).fetch();

        /*TypedQuery<BuildingEntity> queryB = entityManager.createQuery(
                "select b from BuildingEntity b " +
                        "where b in (select a.building, count(a.id) from ApartmentEntity a " +
                        "where lower(a.status) like lower('free') " +
                        "group by a.building)",
                BuildingEntity.class);
        List<BuildingEntity> result = queryB.getResultList();*/
        return result;
    }
}
