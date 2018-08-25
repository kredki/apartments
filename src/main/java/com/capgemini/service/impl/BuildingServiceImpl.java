package com.capgemini.service.impl;

import com.capgemini.service.BuildingService;
import com.capgemini.types.BuildingTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Building service implementation.
 */
@Service
public class BuildingServiceImpl implements BuildingService {
    /**
     * Add new building.
     * @param buildingToAdd Building to add.
     * @return Added building.
     */
    @Override
    @Transactional
    public BuildingTO addNewBuilding(BuildingTO buildingToAdd) {
        return null;
    }

    /**
     * Update building.
     * @param buildingToUpdate Building to update.
     * @return Updated building.
     */
    @Override
    @Transactional
    public BuildingTO updateBuilding(BuildingTO buildingToUpdate) {
        return null;
    }

    /**
     * Remove building.
     * @param buildingId Building id.
     */
    @Override
    @Transactional
    public void removeBuilding(Long buildingId) {

    }

    /**
     * Find all buildings.
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<BuildingTO> findAll() {
        return null;
    }

    /**
     * Find building by id.
     * @param id Building id.
     * @return Requested building.
     */
    @Override
    @Transactional(readOnly = true)
    public BuildingTO findById(Long id) {
        return null;
    }
}
