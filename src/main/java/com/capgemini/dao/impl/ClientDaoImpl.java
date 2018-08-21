package com.capgemini.dao.impl;

import com.capgemini.dao.ClientDao;
import com.capgemini.domain.ClientEntity;
import org.springframework.stereotype.Repository;

/**
 * Client DAO implementation
 */
@Repository
public class ClientDaoImpl extends AbstractDao<ClientEntity, Long> implements ClientDao {
}
