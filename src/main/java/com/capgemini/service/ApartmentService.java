package com.capgemini.service;

import com.capgemini.types.ApartmentTO;

import java.util.List;

/**
 * Apartment service interface.
 */
public interface ApartmentService {
    /**
     * Add new apartment to building.
     * @param apartmentToAdd Apartment to add.
     * @param buildingId Building id.
     * @return Added apartment.
     */
    public ApartmentTO addNewApartment(ApartmentTO apartmentToAdd, Long buildingId);

    /**
     * Update apartment or add new one if not exists.
     * @param apartmentToUpdate Apartment to update.
     * @return Apartment after update.
     */
    public ApartmentTO updateApartment(ApartmentTO apartmentToUpdate);

    /**
     * Remove aparetment.
     * @param apartmentId Apartment id.
     */
    public void removeApartment(Long apartmentId);

    /**
     * Find all apartments.
     * @return All apartments.
     */
    public List<ApartmentTO> findAll();

    /**
     * Find apartment by id.
     * @param id Apartment id.
     * @return Requested apartment.
     */
    public ApartmentTO findById(Long id);
}
