package com.capgemini.service.impl;

import com.capgemini.dao.ApartmentRepository;
import com.capgemini.domain.ApartmentEntity;
import com.capgemini.mappers.ApartmentMapper;
import com.capgemini.service.ApartmentService;
import com.capgemini.types.ApartmentTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Apartment service implementation.
 */
@Service
public class ApartmentServiceImpl implements ApartmentService {
    @Autowired
    ApartmentMapper apartmentMapper;
    @Autowired
    ApartmentRepository apartmentRepository;

    /**
     * Add new apartment to building.
     * @param apartmentToAdd Apartment to add.
     * @param buildingId Building id.
     * @return Added apartment.
     */
    @Override
    public ApartmentTO addNewApartment(ApartmentTO apartmentToAdd, Long buildingId) {
        if(apartmentToAdd == null || buildingId == null) {
            return null;
        }
        apartmentToAdd.setBuilding(buildingId);
        return apartmentMapper.toTO(apartmentRepository.save(apartmentMapper.toEntity(apartmentToAdd)));
    }

    /**
     * Update apartment or add new one if not exists.
     * @param apartmentToUpdate Apartment to update.
     * @return Apartment after update.
     */
    @Override
    public ApartmentTO updateApartment(ApartmentTO apartmentToUpdate) {
        if(apartmentToUpdate == null) {
            return null;
        }
        ApartmentEntity apartmentEntity = apartmentMapper.toEntity(apartmentToUpdate);
        ApartmentEntity savedApartment = apartmentRepository.save(apartmentEntity);
        return apartmentMapper.toTO(savedApartment);
    }

    /**
     * Remove aparetment.
     * @param apartmentId Apartment id.
     */
    @Override
    public void removeApartment(Long apartmentId) {
        if(apartmentRepository.findOne(apartmentId) == null) {
            return;
        }
        apartmentRepository.delete(apartmentId);
    }

    /**
     * Find all apartments.
     * @return All apartments.
     */
    @Override
    public List<ApartmentTO> findAll() {
        return apartmentMapper.map2TOs(apartmentRepository.findAll());
    }

    /**
     * Find apartment by id.
     * @param id Apartment id.
     * @return Requested apartment.
     */
    @Override
    public ApartmentTO findById(Long id) {
        return apartmentMapper.toTO(apartmentRepository.findOne(id));
    }
}
