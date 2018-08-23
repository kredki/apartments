package com.capgemini.dao;

import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ApartmentEntity;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.testutils.ApartmentGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BuildingRepositoryTest {
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    ApartmentRepository apartmentRepository;
    @Autowired
    ApartmentGenerator apartmentGenerator;

    private ApartmentEntity apartment1;
    private ApartmentEntity apartment2;
    private ApartmentEntity apartment3;
    private BuildingEntity building;
    private BigDecimal price1;
    private BigDecimal area1;
    private AddressInTable address;

    @Before
    public void setup() {
        price1 = new BigDecimal("40000.00");
        BigDecimal price2 = new BigDecimal("100000.00");
        area1 = new BigDecimal("12.12");
        BigDecimal area2 = new BigDecimal("130.00");
        BigDecimal area3 = new BigDecimal("58.00");
        address = AddressInTable.builder()
                .city("city")
                .no("no")
                .postalCode("postal code")
                .street("street")
                .build();

        building = BuildingEntity.builder()
                .isElevatorPresent(true)
                .floorQty(10)
                .description("description")
                .apartmentsQty(0)
                .address(address)
                .build();
        Set<ApartmentEntity> apartments = new HashSet<>();
        apartment1 = ApartmentEntity.builder()
                .balconyQty(1)
                .floor(0)
                .status("sold")
                .price(price1)
                .roomQty(3)
                .building(building)
                .area(area2)
                .address(address)
                .build();
        apartments.add(apartment1);

        apartment2 = ApartmentEntity.builder()
                .balconyQty(2)
                .floor(0)
                .status("free")
                .price(price1)
                .roomQty(2)
                .building(building)
                .area(area1)
                .address(address)
                .build();
        apartments.add(apartment2);

        apartment3 = ApartmentEntity.builder()
                .balconyQty(3)
                .floor(0)
                .status("free")
                .price(price2)
                .roomQty(1)
                .building(building)
                .area(area3)
                .address(address)
                .build();
        apartments.add(apartment3);
        building.setApartments(apartments);
        buildingRepository.save(building);
    }

    @Test
    @Transactional
    public void shouldFindBuildingsWithMostFreeApartments() {
        //given

        //building1
        BuildingEntity building1 = BuildingEntity.builder()
                .isElevatorPresent(true)
                .floorQty(10)
                .description("description")
                .apartmentsQty(0)
                .address(address)
                .build();
        Set<ApartmentEntity> apartments1 = new HashSet<>();
        ApartmentEntity apartment1 = apartmentGenerator.getFreeApartment();
        apartment1.setBuilding(building1);
        apartments1.add(apartment1);
        ApartmentEntity apartment2 = apartmentGenerator.getFreeApartment();
        apartment2.setBuilding(building1);
        apartments1.add(apartment2);
        ApartmentEntity apartment3 = apartmentGenerator.getFreeApartment();
        apartment3.setBuilding(building1);
        apartments1.add(apartment3);
        building1.setApartments(apartments1);
        building1 = buildingRepository.save(building1);

        //building2
        BuildingEntity building2 = BuildingEntity.builder()
                .isElevatorPresent(true)
                .floorQty(10)
                .description("description")
                .apartmentsQty(0)
                .address(address)
                .build();
        Set<ApartmentEntity> apartments2 = new HashSet<>();
        ApartmentEntity apartment4 = apartmentGenerator.getFreeApartment();
        apartment4.setBuilding(building1);
        apartments2.add(apartment4);
        ApartmentEntity apartment5 = apartmentGenerator.getFreeApartment();
        apartment5.setBuilding(building1);
        apartments2.add(apartment5);
        ApartmentEntity apartment6 = apartmentGenerator.getFreeApartment();
        apartment6.setBuilding(building1);
        apartments2.add(apartment6);
        building2.setApartments(apartments2);
        building2 = buildingRepository.save(building2);

        //building3
        BuildingEntity building3 = BuildingEntity.builder()
                .isElevatorPresent(true)
                .floorQty(10)
                .description("description")
                .apartmentsQty(0)
                .address(address)
                .build();
        Set<ApartmentEntity> apartments3 = new HashSet<>();
        ApartmentEntity apartment7 = apartmentGenerator.getFreeApartment();
        apartment7.setBuilding(building1);
        apartments3.add(apartment7);
        ApartmentEntity apartment8 = apartmentGenerator.getFreeApartment();
        apartment8.setBuilding(building1);
        apartments3.add(apartment8);
        ApartmentEntity apartment9 = apartmentGenerator.getFreeApartment();
        apartment9.setBuilding(building1);
        apartments3.add(apartment9);
        building3.setApartments(apartments3);
        building3 = buildingRepository.save(building3);

        List<Long> buildingsIds = new ArrayList<>();
        buildingsIds.add(building1.getId());
        buildingsIds.add(building2.getId());
        buildingsIds.add(building3.getId());

        //when
        List<BuildingEntity> result = buildingRepository.findBuildingWithMostFreeApartments();

        //then
        assertNotNull(result);
        assertEquals(3, result.size());
        List<Long> resultIds = new ArrayList<>();
        for (BuildingEntity building : result) {
            resultIds.add(building.getId());
        }
        for (Long id : buildingsIds) {
            assertTrue(resultIds.contains(id));
        }

    }

    @Test
    @Transactional
    public void shouldCountByStatus() {
        //given
        String status = "free";
        Long expectedCount = 0L;
        List<ApartmentEntity> apartments = apartmentRepository.findAll();
        Long buildingId = building.getId();
        for (ApartmentEntity apartment : apartments) {
            if(apartment.getBuilding().getId() == buildingId
                    && apartment.getStatus().toUpperCase().equals(status.toUpperCase())) {
                expectedCount++;
            }
        }

        //when
        Long result = buildingRepository.countByStatus(buildingId, status);

        //then
        assertEquals(expectedCount, result);
    }

    @Test
    public void shouldFindAvgPrice() {
        //given
        BigDecimal expectedAvg = new BigDecimal("60000");

        //when
        BigDecimal result = buildingRepository.findAvgPriceForBuilding(building.getId());

        //then
        assertEquals(expectedAvg, result);
    }

    @Test
    public void shouldAddBuilding() {
        //given
        long clientsQty = buildingRepository.count();
        AddressInTable address = AddressInTable.builder().city("city").no("no").postalCode("code").street("street").build();
        BuildingEntity building = BuildingEntity.builder()
                .address(address)
                .apartmentsQty(0)
                .description("description")
                .floorQty(0)
                .isElevatorPresent(true)
                .build();

        //when
        BuildingEntity savedBuilding = buildingRepository.save(building);

        //then
        assertEquals(clientsQty + 1, buildingRepository.count());
        assertEquals(building.getApartmentsQty(), savedBuilding.getApartmentsQty());
        assertEquals(building.getDescription(), savedBuilding.getDescription());
        assertEquals(building.getFloorQty(), savedBuilding.getApartmentsQty());
        assertEquals(building.getIsElevatorPresent(), savedBuilding.getIsElevatorPresent());
    }
}