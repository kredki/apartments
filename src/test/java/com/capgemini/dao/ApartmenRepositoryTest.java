package com.capgemini.dao;

import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ApartmentEntity;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.types.ApartmentSearchCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ApartmenRepositoryTest {
    @Autowired
    private ApartmentRepository apartmenRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    private ApartmentEntity apartment1;
    private ApartmentEntity apartment2;
    private ApartmentEntity apartment3;

    @Before
    public void setup() {
        BigDecimal price1 = new BigDecimal("123000.12");
        BigDecimal price2 = new BigDecimal("123456.12");
        BigDecimal area1 = new BigDecimal("12.12");
        BigDecimal area2 = new BigDecimal("130.00");
        BigDecimal area3 = new BigDecimal("58.00");
        AddressInTable address = AddressInTable.builder()
                .city("city")
                .no("no")
                .postalCode("postal code")
                .street("street")
                .build();

        BuildingEntity building = BuildingEntity.builder()
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
    public void shouldAddApartment() {
        //given
        long clientsQty = apartmenRepository.count();
        AddressInTable address = AddressInTable.builder().city("city").no("no").postalCode("code").street("street").build();
        ApartmentEntity apertment = ApartmentEntity.builder().address(address).area(new BigDecimal("5000"))
                .roomQty(10).price(new BigDecimal("1.29")).status("FREE").floor(0).balconyQty(12).build();

        //when
        ApartmentEntity savedApartment = apartmenRepository.save(apertment);

        //then
        assertEquals(clientsQty + 1, apartmenRepository.count());
        assertEquals(apertment.getArea(), savedApartment.getArea());
        assertEquals(apertment.getBalconyQty(), savedApartment.getBalconyQty());
        assertEquals(apertment.getFloor(), savedApartment.getFloor());
        assertEquals(apertment.getPrice(), savedApartment.getPrice());
    }

    @Test
    public void shouldFindByCriteria() {
        //given
        ApartmentSearchCriteria criteria = ApartmentSearchCriteria.builder()
                .areaFrom(new BigDecimal("58.00"))
                .areaTo(new BigDecimal("130.00"))
                .balconyQtyFrom(2)
                .balconyQtyTo(3)
                .roomQtyFrom(1)
                .roomQtyTo(5)
                .build();

        //when
        List<ApartmentEntity> result = apartmenRepository.findApartmentsByCriteria(criteria);

        //then
        assertEquals(1, result.size());
        ApartmentEntity apartment = result.get(0);
        assertEquals(apartment.getId(), apartment3.getId());
        assertEquals(apartment.getPrice(), apartment3.getPrice());
        assertEquals(apartment.getFloor(), apartment3.getFloor());
        assertEquals(apartment.getBalconyQty(), apartment3.getBalconyQty());
        assertEquals(apartment.getArea(), apartment3.getArea());
        assertEquals(apartment.getRoomQty(), apartment3.getRoomQty());
        assertEquals(apartment.getStatus().toUpperCase(), "free".toUpperCase());
    }

    @Test
    public void shouldNotFindByCriteria() {
        //given
        ApartmentSearchCriteria criteria = ApartmentSearchCriteria.builder()
                .build();

        //when
        List<ApartmentEntity> result = apartmenRepository.findApartmentsByCriteria(criteria);

        //then
        assertEquals(0, result.size());
    }

    @Test
    public void shouldFindByRoomQtyFrom() {
        //given
        ApartmentSearchCriteria criteria = ApartmentSearchCriteria.builder()
                .roomQtyFrom(1)
                .build();

        //when
        List<ApartmentEntity> result = apartmenRepository.findApartmentsByCriteria(criteria);

        //then
        assertEquals(2, result.size());
        Set<Long> ids = new HashSet<>();
        for (ApartmentEntity apartment : result) {
            ids.add(apartment.getId());
        }
        assertTrue(ids.contains(apartment2.getId()));
        assertTrue(ids.contains(apartment3.getId()));
    }

    @Test
    public void shouldFindByRoomQtyTo() {
        //given
        ApartmentSearchCriteria criteria = ApartmentSearchCriteria.builder()
                .roomQtyTo(10)
                .build();

        //when
        List<ApartmentEntity> result = apartmenRepository.findApartmentsByCriteria(criteria);

        //then
        assertEquals(2, result.size());
        Set<Long> ids = new HashSet<>();
        for (ApartmentEntity apartment : result) {
            ids.add(apartment.getId());
        }
        assertTrue(ids.contains(apartment2.getId()));
        assertTrue(ids.contains(apartment3.getId()));
    }

    @Test
    public void shouldFindByBalconyQtyTo() {
        //given
        ApartmentSearchCriteria criteria = ApartmentSearchCriteria.builder()
                .balconyQtyTo(10)
                .build();

        //when
        List<ApartmentEntity> result = apartmenRepository.findApartmentsByCriteria(criteria);

        //then
        assertEquals(2, result.size());
        Set<Long> ids = new HashSet<>();
        for (ApartmentEntity apartment : result) {
            ids.add(apartment.getId());
        }
        assertTrue(ids.contains(apartment2.getId()));
        assertTrue(ids.contains(apartment3.getId()));
    }

    @Test
    public void shouldFindByBalconyQtyFrom() {
        //given
        ApartmentSearchCriteria criteria = ApartmentSearchCriteria.builder()
                .balconyQtyFrom(0)
                .build();

        //when
        List<ApartmentEntity> result = apartmenRepository.findApartmentsByCriteria(criteria);

        //then
        assertEquals(2, result.size());
        Set<Long> ids = new HashSet<>();
        for (ApartmentEntity apartment : result) {
            ids.add(apartment.getId());
        }
        assertTrue(ids.contains(apartment2.getId()));
        assertTrue(ids.contains(apartment3.getId()));
    }

    @Test
    public void shouldFindByAreaFrom() {
        //given
        ApartmentSearchCriteria criteria = ApartmentSearchCriteria.builder()
                .areaFrom(new BigDecimal("12.12"))
                .build();

        //when
        List<ApartmentEntity> result = apartmenRepository.findApartmentsByCriteria(criteria);

        //then
        assertEquals(2, result.size());
        Set<Long> ids = new HashSet<>();
        for (ApartmentEntity apartment : result) {
            ids.add(apartment.getId());
        }
        assertTrue(ids.contains(apartment2.getId()));
        assertTrue(ids.contains(apartment3.getId()));
    }

    @Test
    public void shouldFindByAreaTo() {
        //given
        ApartmentSearchCriteria criteria = ApartmentSearchCriteria.builder()
                .areaTo(new BigDecimal("130.00"))
                .build();

        //when
        List<ApartmentEntity> result = apartmenRepository.findApartmentsByCriteria(criteria);

        //then
        assertEquals(2, result.size());
        Set<Long> ids = new HashSet<>();
        for (ApartmentEntity apartment : result) {
            ids.add(apartment.getId());
        }
        assertTrue(ids.contains(apartment2.getId()));
        assertTrue(ids.contains(apartment3.getId()));
    }
}