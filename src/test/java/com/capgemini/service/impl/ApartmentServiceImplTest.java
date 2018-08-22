package com.capgemini.service.impl;

import com.capgemini.dao.BuildingRepository;
import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ApartmentEntity;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.types.AddressTO;
import com.capgemini.types.ApartmentTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
public class ApartmentServiceImplTest {
    @Autowired
    private ApartmentServiceImpl apartmentService;
    @Autowired
    private BuildingRepository buildingRepository;

    private BuildingEntity building;

    @Before
    public void setup() {
        AddressInTable address = AddressInTable.builder()
                .street("street")
                .postalCode("code")
                .no("no").city("city")
                .build();
        building = BuildingEntity.builder()
                .address(address)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        building = buildingRepository.save(building);
    }

    @Test
    public void shouldAddApartment() {
        //given
        Set<ApartmentEntity>apartments = building.getApartments();
        Integer apartmentsQtyBefore = apartments.size();
        AddressTO address = AddressTO.builder()
                .city("c").no("n")
                .postalCode("pc")
                .street("s")
                .build();
        ApartmentTO apartmentToAdd = ApartmentTO.builder()
                .address(address).area(new BigDecimal("50"))
                .balconyQty(1)
                .building(null)
                .floor(0).price(new BigDecimal("120000"))
                .status("free")
                .build();

        //when
        ApartmentTO addedApartment = apartmentService.addNewApartment(apartmentToAdd, building.getId());

        //then
        assertNotNull(addedApartment);
        Long addedApartmentId = addedApartment.getId();
        assertNotNull(addedApartmentId);
        BuildingEntity buildingAfter = buildingRepository.findOne(building.getId());
        Set<ApartmentEntity> apartmentsAfter = buildingAfter.getApartments();
        assertEquals(apartmentsQtyBefore + 1, apartmentsAfter.size());
        assertEquals(new Integer(apartmentsQtyBefore + 1), buildingAfter.getApartmentsQty());
        Set<Long> ids = new HashSet<>();
        for (ApartmentEntity apartment : apartmentsAfter) {
            ids.add(apartment.getId());
        }
        assertTrue(ids.contains(addedApartmentId));
        for (ApartmentEntity apartment : apartmentsAfter) {
            if(apartment.getId() == addedApartmentId) {
                assertEquals(apartmentToAdd.getArea(), apartment.getArea());
                assertEquals(apartmentToAdd.getBalconyQty(), apartment.getBalconyQty());
                assertEquals(apartmentToAdd.getFloor(), apartment.getFloor());
                assertEquals(apartmentToAdd.getPrice(), apartment.getPrice());
                assertEquals(apartmentToAdd.getRoomQty(), apartment.getRoomQty());
                assertEquals(apartmentToAdd.getStatus(), apartment.getStatus());
            }
        }
    }
}