package com.capgemini.service.impl;

import com.capgemini.service.BuildingService;
import com.capgemini.types.BuildingTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingServiceImpl implements BuildingService {
    @Override
    public BuildingTO addNewBuilding(BuildingTO buildingToAdd) {
        return null;
    }

    @Override
    public BuildingTO updateBuilding(BuildingTO buildingToUpdate) {
        return null;
    }

    @Override
    public void removeBuilding(Long buildingId) {

    }

    @Override
    public List<BuildingTO> findAll() {
        return null;
    }

    @Override
    public BuildingTO findById(Long id) {
        return null;
    }
}
