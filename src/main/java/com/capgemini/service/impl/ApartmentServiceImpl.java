package com.capgemini.service.impl;

import com.capgemini.dao.ApartmentRepository;
import com.capgemini.dao.ClientRepository;
import com.capgemini.domain.ApartmentEntity;
import com.capgemini.domain.ClientEntity;
import com.capgemini.mappers.ApartmentMapper;
import com.capgemini.service.ApartmentService;
import com.capgemini.types.ApartmentTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Apartment service implementation.
 */
@Service
public class ApartmentServiceImpl implements ApartmentService {
    @Autowired
    private ApartmentMapper apartmentMapper;
    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private ClientRepository clientRepository;

    /**
     * Add new apartment to building.
     * @param apartmentToAdd Apartment to add.
     * @param buildingId Building id.
     * @return Added apartment.
     */
    @Override
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional(readOnly = true)
    public List<ApartmentTO> findAll() {
        return apartmentMapper.map2TOs(apartmentRepository.findAll());
    }

    /**
     * Find apartment by id.
     * @param id Apartment id.
     * @return Requested apartment.
     */
    @Override
    @Transactional(readOnly = true)
    public ApartmentTO findById(Long id) {
        return apartmentMapper.toTO(apartmentRepository.findOne(id));
    }

    /**
     * Add reservation for apartment.
     * @param clientId Client id.
     * @param apartmentId Apartment id.
     * @return True if successful, false if not.
     */
    @Override
    @Transactional
    public boolean addReservation(Long clientId, Long apartmentId) {
        ApartmentEntity apartment = apartmentRepository.findOne(apartmentId);
        ClientEntity client = clientRepository.findOne(clientId);
        if(apartment == null || client == null) {
            return false;
        }
        int reservedApartmentsQty = apartmentRepository.findReservedApartmentsForClient(clientId).size();
        if(reservedApartmentsQty <= 3 && apartment.getStatus().toLowerCase().equals("free") &&
                apartment.getMainOwner() == null) {
            apartment.setMainOwner(client);
            apartment.addOwner(client);
            client.addApartment(apartment);
            apartmentRepository.save(apartment);
            clientRepository.save(client);
            return true;
        }
        return false;
    }
}
