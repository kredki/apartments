package com.capgemini.service.impl;

import com.capgemini.dao.BuildingRepository;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.mappers.BuildingMapper;
import com.capgemini.service.BuildingService;
import com.capgemini.types.BuildingTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Building service implementation.
 */
@Service
public class BuildingServiceImpl implements BuildingService {
    @Autowired
    BuildingMapper buildingMapper;
    @Autowired
    BuildingRepository buildingRepository;
    /**
     * Add new building.
     * @param buildingToAdd Building to add.
     * @return Added building.
     */
    @Override
    @Transactional
    public BuildingTO addNewBuilding(BuildingTO buildingToAdd) {
        if(buildingToAdd == null) {
            return null;
        }
        BuildingEntity buildingEntity = buildingMapper.toEntity(buildingToAdd);
        return buildingMapper.toTO(buildingRepository.save(buildingEntity));
    }

    /**
     * Update building.
     * @param buildingToUpdate Building to update.
     * @return Updated building.
     */
    @Override
    @Transactional
    public BuildingTO updateBuilding(BuildingTO buildingToUpdate) {
        if(buildingToUpdate == null) {
            return null;
        }
        BuildingEntity buildingEntity = buildingMapper.toEntity(buildingToUpdate);
        return buildingMapper.toTO(buildingRepository.save(buildingEntity));
    }

    /**
     * Remove building.
     * @param buildingId Building id.
     */
    @Override
    @Transactional
    public void removeBuilding(Long buildingId) {
        buildingRepository.delete(buildingId);
    }

    /**
     * Find all buildings.
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<BuildingTO> findAll() {
        return buildingMapper.map2TOs(buildingRepository.findAll());
    }

    /**
     * Find building by id.
     * @param id Building id.
     * @return Requested building.
     */
    @Override
    @Transactional(readOnly = true)
    public BuildingTO findById(Long id) {
        return buildingMapper.toTO(buildingRepository.findOne(id));
    }
}
