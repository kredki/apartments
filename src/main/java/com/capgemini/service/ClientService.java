package com.capgemini.service;

import com.capgemini.types.ClientTO;

import java.util.List;

/**
 * Client service.
 */
public interface ClientService {
    /**
     * Add new client.
     * @param clientToAdd Client to add.
     * @return Added client.
     */
    public ClientTO addNewClient(ClientTO clientToAdd);

    /**
     * Update client.
     * @param clientToUpdate Client to update.
     * @return Updated client.
     */
    public ClientTO updateClient(ClientTO clientToUpdate);

    /**
     * Remove client.
     * @param clientId Client id.
     */
    public void removeClient(Long clientId);

    /**
     * Find all clients.
     * @return All clients.
     */
    public List<ClientTO> findAll();

    /**
     * Find client by id.
     * @param id Client id.
     * @return Requested client.
     */
    public ClientTO findById(Long id);
}
