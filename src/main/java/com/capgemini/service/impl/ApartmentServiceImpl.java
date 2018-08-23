package com.capgemini.service.impl;

import com.capgemini.dao.ApartmentRepository;
import com.capgemini.mappers.ApartmentMapper;
import com.capgemini.service.ApartmentService;
import com.capgemini.types.ApartmentTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApartmentServiceImpl implements ApartmentService {
    @Autowired
    ApartmentMapper apartmentMapper;
    @Autowired
    ApartmentRepository apartmentRepository;

    @Override
    public ApartmentTO addNewApartment(ApartmentTO apartmentToAdd, Long buildingId) {
        if(apartmentToAdd == null) {
            return null;
        }
        apartmentToAdd.setBuilding(buildingId);
        return apartmentMapper.toTO(apartmentRepository.save(apartmentMapper.toEntity(apartmentToAdd)));
    }

    @Override
    public ApartmentTO updateApartment(ApartmentTO apartmentToUpdate) {
        return apartmentMapper.toTO(apartmentRepository.save(apartmentMapper.toEntity(apartmentToUpdate)));
    }

    @Override
    public void removeApartment(Long apartmentId) {
        apartmentRepository.delete(apartmentId);
    }

    @Override
    public List<ApartmentTO> findAll() {
        return apartmentMapper.map2TOs(apartmentRepository.findAll());
    }

    @Override
    public ApartmentTO findById(Long id) {
        return apartmentMapper.toTO(apartmentRepository.findOne(id));
    }
}
