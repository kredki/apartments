package com.capgemini.service.impl;

import com.capgemini.dao.ClientRepository;
import com.capgemini.mappers.ClientMapper;
import com.capgemini.service.ClientService;
import com.capgemini.types.ClientTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientMapper clientMapper;

    @Override
    @Transactional(propagation=Propagation.REQUIRED, noRollbackFor=Exception.class)
    public ClientTO addNewClient(ClientTO clientToAdd) {
        if(clientToAdd == null) {
            return null;
        }
        return clientMapper.toTO(clientRepository.save(clientMapper.toEntity(clientToAdd)));
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED, noRollbackFor=Exception.class)
    public ClientTO updateClient(ClientTO clientToUpdate) {
        if(clientToUpdate == null) {
            return null;
        }
        return clientMapper.toTO(clientRepository.save(clientMapper.toEntity(clientToUpdate)));
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED, noRollbackFor=Exception.class)
    public void removeClient(Long clientId) {
        clientRepository.delete(clientId);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public List<ClientTO> findAll() {
        return clientMapper.map2TOs(clientRepository.findAll());
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public ClientTO findById(Long id) {
        return clientMapper.toTO(clientRepository.findOne(id));
    }
}
