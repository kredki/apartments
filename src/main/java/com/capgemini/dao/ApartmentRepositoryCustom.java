package com.capgemini.dao;

import com.capgemini.domain.ApartmentEntity;
import com.capgemini.types.ApartmentSearchCriteria;

import java.util.List;

public interface ApartmentRepositoryCustom {
    public List<ApartmentEntity> findApartmentsByMainOwner(long mainOwnerId);

    public List<ApartmentEntity> findApartmentsByCriteria(ApartmentSearchCriteria criteria);
}
