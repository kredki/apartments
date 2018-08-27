package com.capgemini.service.impl;

import com.capgemini.dao.ClientRepository;
import com.capgemini.exceptions.VersionIsNullException;
import com.capgemini.mappers.ClientMapper;
import com.capgemini.service.ClientService;
import com.capgemini.types.ClientTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Client service.
 */
@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientMapper clientMapper;

    /**
     * Add new client.
     * @param clientToAdd Client to add.
     * @return Added client.
     */
    @Override
    @Transactional
    public ClientTO addNewClient(ClientTO clientToAdd) {
        if(clientToAdd == null) {
            return null;
        }
        return clientMapper.toTO(clientRepository.save(clientMapper.toEntity(clientToAdd)));
    }

    /**
     * Update client.
     * @param clientToUpdate Client to update.
     * @return Updated client.
     */
    @Override
    @Transactional
    public ClientTO updateClient(ClientTO clientToUpdate) {
        if(clientToUpdate == null) {
            return null;
        }
        if(clientToUpdate.getVersion() == null) {
            throw new VersionIsNullException("Null version in ClientTO");
        }
        return clientMapper.toTO(clientRepository.save(clientMapper.toEntity(clientToUpdate)));
    }

    /**
     * Remove client.
     * @param clientId Client id.
     */
    @Override
    @Transactional
    public void removeClient(Long clientId) {
        if (clientId != null) {
            clientRepository.delete(clientId);
        }
    }

    /**
     * Find all clients.
     * @return All clients.
     */
    @Override
    @Transactional(readOnly=true)
    public List<ClientTO> findAll() {
        return clientMapper.map2TOs(clientRepository.findAll());
    }

    /**
     * Find client by id.
     * @param id Client id.
     * @return Requested client.
     */
    @Override
    @Transactional(readOnly=true)
    public ClientTO findById(Long id) {
        return clientMapper.toTO(clientRepository.findOne(id));
    }
}
