package com.capgemini.service;

import com.capgemini.types.BuildingTO;

import java.util.List;

public interface BuildingService {
    public BuildingTO addNewBuilding(BuildingTO buildingToAdd);

    public BuildingTO updateBuilding(BuildingTO buildingToUpdate);

    public void removeBuilding(Long buildingId);

    public List<BuildingTO> findAll();

    public BuildingTO findById(Long id);
}
