package com.capgemini.service;

import com.capgemini.types.BuildingTO;

import java.util.List;

/**
 * Building service
 */
public interface BuildingService {
    /**
     * Add new building.
     * @param buildingToAdd Building to add.
     * @return Added building.
     */
    public BuildingTO addNewBuilding(BuildingTO buildingToAdd);

    /**
     * Update building.
     * @param buildingToUpdate Building to update.
     * @return Updated building.
     */
    public BuildingTO updateBuilding(BuildingTO buildingToUpdate);

    /**
     * Remove building.
     * @param buildingId Building id.
     */
    public void removeBuilding(Long buildingId);

    /**
     * Find all buildings.
     * @return
     */
    public List<BuildingTO> findAll();

    /**
     * Find building by id.
     * @param id Building id.
     * @return Requested building.
     */
    public BuildingTO findById(Long id);
}
