package com.capgemini.dao;

import com.capgemini.domain.ClientEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Client DAO
 */
public interface ClientDao extends CrudRepository<ClientEntity, Long> {
}
