package com.capgemini.dao;

import com.capgemini.domain.ApartmentEntity;
import org.springframework.data.repository.CrudRepository;

public interface ApartmentDAO extends CrudRepository<ApartmentEntity, Long> {
}
