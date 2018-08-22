package com.capgemini.dao;

import com.capgemini.domain.ApartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApartmentRepository extends JpaRepository<ApartmentEntity, Long>, ApartmentRepositoryCustom {
}
