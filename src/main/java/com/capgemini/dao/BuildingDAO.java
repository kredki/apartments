package com.capgemini.dao;

import com.capgemini.domain.BuildingEntity;
import org.springframework.data.repository.CrudRepository;

public interface BuildingDAO extends CrudRepository<BuildingEntity, Long> {
}
