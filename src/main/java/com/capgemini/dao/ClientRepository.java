package com.capgemini.dao;

import com.capgemini.domain.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Client DAO
 */
public interface ClientRepository extends JpaRepository<ClientEntity, Long>, ClientRepositoryCustom {
}
