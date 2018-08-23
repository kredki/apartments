package com.capgemini.dao;

import com.capgemini.domain.BuildingEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Custom interface for building repository.
 */
public interface BuildingRepositoryCustom {
    /**
     * Find average price in given building.
     * @param buildingId Building id.
     * @return Average price in given building.
     */
    public BigDecimal findAvgPriceForBuilding(Long buildingId);

    /**
     * Return number of apartments of given status in given building.
     * @param buildingId Building id.
     * @param status Status of apartment.
     * @return Number of apartments of given status in given building.
     */
    public Long countByStatus(Long buildingId, String status);

    /**
     * Find building with most number of free apartments.
     * @return Building with most number of free apartments.
     */
    public List<BuildingEntity> findBuildingWithMostFreeApartments();
}
