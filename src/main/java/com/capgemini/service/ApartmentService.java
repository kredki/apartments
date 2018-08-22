package com.capgemini.service;

import com.capgemini.types.ApartmentTO;

import java.util.List;

public interface ApartmentService {
    public ApartmentTO addNewApartment(ApartmentTO apartmentToAdd, Long buildingId);

    public ApartmentTO updateApartment(ApartmentTO apartmentToUpdate);

    public void removeApartment(Long apartmentId);

    public List<ApartmentTO> findAll();

    public ApartmentTO findById(Long id);
}
