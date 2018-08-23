package com.capgemini.dao;

import com.capgemini.domain.ApartmentEntity;
import com.capgemini.types.ApartmentSearchCriteria;

import java.util.List;

/**
 * Interface for apartment repository.
 */
public interface ApartmentRepositoryCustom {
    /**
     * Find apartments by main owner.
     * @param mainOwnerId Client id.
     * @return Apartments for given main owner.
     */
    public List<ApartmentEntity> findApartmentsByMainOwner(long mainOwnerId);

    /**
     * Find apartments by search criteria:
     * -room quantity,
     * -balcony quantity,
     * -area.
     * All criteria can be null.
     * @param criteria Object containing search criteria.
     * @return Apartments matched search criteria.
     */
    public List<ApartmentEntity> findApartmentsByCriteria(ApartmentSearchCriteria criteria);

    /**
     * Find apartments with elevator in building or on ground floor.
     * @return Apartments with elevator in building or on ground floor.
     */
    public List<ApartmentEntity> findApartmentsForDisabled();
}
