package com.capgemini.service;

import com.capgemini.types.ClientTO;

import java.util.List;

public interface ClientService {
    public ClientTO addNewClient(ClientTO clientToAdd);

    public ClientTO updateClient(ClientTO clientToUpdate);

    public void removeClient(Long clientId);

    public List<ClientTO> findAll();

    public ClientTO findById(Long id);
}
