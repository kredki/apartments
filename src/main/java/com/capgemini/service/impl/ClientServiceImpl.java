package com.capgemini.service.impl;

import com.capgemini.service.ClientService;
import com.capgemini.types.ClientTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    @Override
    public ClientTO addNewClient(ClientTO clientToAdd) {
        return null;
    }

    @Override
    public ClientTO updateClient(ClientTO clientToUpdate) {
        return null;
    }

    @Override
    public void removeClient(Long clientId) {

    }

    @Override
    public List<ClientTO> findAll() {
        return null;
    }

    @Override
    public ClientTO findById(Long id) {
        return null;
    }
}
