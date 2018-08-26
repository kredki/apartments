package com.capgemini.dao;

import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ApartmentEntity;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.domain.ClientEntity;
import com.capgemini.testutils.ApartmentGenerator;
import com.capgemini.testutils.BuildingGenerator;
import com.capgemini.testutils.ClientGenerator;
import com.capgemini.types.ApartmentSearchCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ApartmenRepositoryTest {
    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    ApartmentGenerator apartmentGenerator;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    private BuildingGenerator buildingGenerator;
    @Autowired
    private ClientGenerator clientGenerator;

    private ApartmentEntity apartment1;
    private ApartmentEntity apartment2;
    private ApartmentEntity apartment3;
    private AddressInTable address;

    @Before
    public void setup() {
        BigDecimal price1 = new BigDecimal("123000.12");
        BigDecimal price2 = new BigDecimal("123456.12");
        BigDecimal area1 = new BigDecimal("12.12");
        BigDecimal area2 = new BigDecimal("130.00");
        BigDecimal area3 = new BigDecimal("58.00");
        address = AddressInTable.builder()
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
                .floor(1)
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
    public void shouldReturnApartmentsForClient() {
        //given
        ClientEntity client1 = clientGenerator.getClient();
        ClientEntity client2 = clientGenerator.getClient();
        Set<ClientEntity> owners = new HashSet<>();
        owners.add(client1);
        owners.add(client2);

        BuildingEntity building = buildingGenerator.getBuilding();

        Set<ApartmentEntity> client1Apartments = new HashSet<>();
        Set<ApartmentEntity> client2Apartments = new HashSet<>();

        ApartmentEntity apartment1 = apartmentGenerator.getReservedApartment();
        apartment1.setMainOwner(client1);
        apartment1.setOwners(Collections.singleton(client1));
        apartment1.setBuilding(building);
        apartment1 = apartmentRepository.save(apartment1);
        client1Apartments.add(apartment1);

        ApartmentEntity apartment2 = apartmentGenerator.getReservedApartment();
        apartment2.setMainOwner(client1);
        apartment2.setOwners(owners);
        apartment2.setBuilding(building);
        apartment2 = apartmentRepository.save(apartment2);
        client1Apartments.add(apartment2);
        client2Apartments.add(apartment2);

        ApartmentEntity apartment3 = apartmentGenerator.getReservedApartment();
        apartment3.setMainOwner(client2);
        apartment3.setOwners(owners);
        apartment3.setBuilding(building);
        apartment3 = apartmentRepository.save(apartment3);
        client1Apartments.add(apartment3);
        client2Apartments.add(apartment3);

        ApartmentEntity apartment4 = apartmentGenerator.getSoldApartment();
        apartment4.setMainOwner(client1);
        apartment4.setOwners(owners);
        apartment4.setBuilding(building);
        apartment4 = apartmentRepository.save(apartment4);
        client1Apartments.add(apartment4);
        client2Apartments.add(apartment4);

        ApartmentEntity apartment5 = apartmentGenerator.getSoldApartment();
        apartment5.setMainOwner(client2);
        apartment5.setOwners(owners);
        apartment5.setBuilding(building);
        apartment5 = apartmentRepository.save(apartment5);
        client1Apartments.add(apartment5);
        client2Apartments.add(apartment5);

        client1 = clientRepository.save(client1);
        client2 = clientRepository.save(client2);

        //when
        List<ApartmentEntity> result = apartmentRepository.findReservedApartmentsForClient(client1.getId());

        //then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        List<Long> ids = result.stream().map(x -> x.getId()).collect(Collectors.toList());
        assertTrue(ids.contains(apartment1.getId()));
        assertTrue(ids.contains(apartment2.getId()));
        assertFalse(ids.contains(apartment3.getId()));
        assertFalse(ids.contains(apartment4.getId()));
        assertFalse(ids.contains(apartment5.getId()));
    }

    @Test
    public void shouldNotReturnApartmentsForClient() {
        //when
        List<ApartmentEntity> result = apartmentRepository.findReservedApartmentsForClient(null);

        //then
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void shouldNotReturnApartmentsForClient2() {
        //given
        List<ClientEntity> clientsBefore = clientRepository.findAll();
        List<Long> idsBefore = clientsBefore.stream().map(x -> x.getId()).collect(Collectors.toList());
        Optional<Long> max = idsBefore.stream().max(Comparator.naturalOrder());
        Long idNotInDB = 1L;
        if (max.isPresent()) {
            idNotInDB = max.get() + 1;
        }

        //when
        List<ApartmentEntity> result = apartmentRepository.findReservedApartmentsForClient(idNotInDB);

        //then
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void shouldNotReturnApartmentsForClient3() {
        //when
        List<ApartmentEntity> result = apartmentRepository.findReservedApartmentsForClient(-1L);

        //then
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    @Transactional
    public void shouldFindApartmentByMainOwner() {
        //given
        ClientEntity mainOwner = ClientEntity.builder()
                .telephone("tel1")
                .lastName("Iksinski")
                .firstName("Andrzej")
                .address(address)
                .build();
        ClientEntity coowner = ClientEntity.builder()
                .telephone("tel2")
                .lastName("Nowak")
                .firstName("Jan")
                .address(address)
                .build();

        Set<ApartmentEntity> apartments = new HashSet<>();
        ApartmentEntity apartment1 = apartmentGenerator.getSoldApartmentWithFloor0();
        apartments.add(apartment1);
        ApartmentEntity apartment2 = apartmentGenerator.getSoldApartmentWithFloor1();
        apartments.add(apartment2);
        apartment1.setMainOwner(mainOwner);
        apartment1.setOwners(Collections.singleton(mainOwner));
        apartment2.setMainOwner(mainOwner);
        Set<ClientEntity> owners = new HashSet<>();
        owners.add(mainOwner);
        owners.add(coowner);
        apartment2.setOwners(owners);
        mainOwner.setApartments(apartments);

        ApartmentEntity apartment3 = apartmentGenerator.getSoldApartmentWithFloor1();
        apartment3.setMainOwner(coowner);
        apartment3.setOwners(owners);

        mainOwner = clientRepository.save(mainOwner);
        coowner = clientRepository.save(coowner);

        apartment1 = apartmentRepository.save(apartment1);
        apartment2 = apartmentRepository.save(apartment2);
        apartment3 = apartmentRepository.save(apartment3);

        //when
        List<ApartmentEntity> result = apartmentRepository.findApartmentsByMainOwner(mainOwner.getId());

        //then
        assertThat(result).isNotNull().isNotEmpty();
        assertThat(result.size()).isEqualTo(2);
        List<Long> ids = result.stream().map(x -> x.getId()).collect(Collectors.toList());
        assertTrue(ids.contains(apartment1.getId()));
        assertTrue(ids.contains(apartment2.getId()));
        assertFalse(ids.contains(apartment3.getId()));
    }

    @Test
    @Transactional
    public void shouldFindApartmentsForDisabled() {
        //given
        BuildingEntity building1 = BuildingEntity.builder()
                .isElevatorPresent(false)
                .floorQty(10)
                .description("description")
                .apartmentsQty(0)
                .address(address)
                .build();

        Set<ApartmentEntity> apartments = new HashSet<>();
        ApartmentEntity apartment1 = apartmentGenerator.getSoldApartmentWithFloor0();
        apartments.add(apartment1);
        ApartmentEntity apartment2 = apartmentGenerator.getSoldApartmentWithFloor1();
        apartments.add(apartment2);

        building1.setApartments(apartments);
        apartment1.setBuilding(building1);
        apartment2.setBuilding(building1);
        buildingRepository.save(building1);

        //get apartments ids for disabled
        List<Long> apartmentsFodDisabledIds = new ArrayList<>();
        List<BuildingEntity> buildings = buildingRepository.findAll();
        List<ApartmentEntity> a = apartmentRepository.findAll();
        for (BuildingEntity building : buildings) {
            Set<ApartmentEntity> apartmentsFromBuilding = building.getApartments();
            Boolean isElevatorPresent = building.getIsElevatorPresent();
            for (ApartmentEntity apartment : apartmentsFromBuilding) {
                if (isElevatorPresent || apartment.getFloor() == 0) {
                    apartmentsFodDisabledIds.add(apartment.getId());
                }
            }
        }

        //when
        List<ApartmentEntity> apartmentsForDisabledResult = apartmentRepository.findApartmentsForDisabled();

        //then
        assertThat(apartmentsForDisabledResult).isNotNull().isNotEmpty();
        assertThat(apartmentsForDisabledResult.size()).isEqualTo(apartmentsFodDisabledIds.size());
        List<Long> apartmentForDisabledIdsResult = new ArrayList<>();
        for (ApartmentEntity apartment : apartmentsForDisabledResult) {
            apartmentForDisabledIdsResult.add(apartment.getId());
        }

        for (Long id : apartmentsFodDisabledIds) {
            assertTrue(apartmentForDisabledIdsResult.contains(id));
        }
    }

    @Test
    public void shouldAddApartment() {
        //given
        long clientsQty = apartmentRepository.count();
        AddressInTable address = AddressInTable.builder().city("city").no("no").postalCode("code").street("street").build();
        ApartmentEntity apertment = ApartmentEntity.builder().address(address).area(new BigDecimal("5000"))
                .roomQty(10).price(new BigDecimal("1.29")).status("FREE").floor(0).balconyQty(12).build();

        //when
        ApartmentEntity savedApartment = apartmentRepository.save(apertment);

        //then
        assertEquals(clientsQty + 1, apartmentRepository.count());
        assertEquals(apertment.getArea(), savedApartment.getArea());
        assertEquals(apertment.getBalconyQty(), savedApartment.getBalconyQty());
        assertEquals(apertment.getFloor(), savedApartment.getFloor());
        assertEquals(apertment.getPrice(), savedApartment.getPrice());
    }

    @Test
    public void shouldFindByNullCriteria() {
        //when
        List<ApartmentEntity> result = apartmentRepository.findApartmentsByCriteria(null);

        //then
        assertThat(result).isNotNull().isEmpty();
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
        List<ApartmentEntity> result = apartmentRepository.findApartmentsByCriteria(criteria);

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
        List<ApartmentEntity> result = apartmentRepository.findApartmentsByCriteria(criteria);

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
        List<ApartmentEntity> result = apartmentRepository.findApartmentsByCriteria(criteria);

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
        List<ApartmentEntity> result = apartmentRepository.findApartmentsByCriteria(criteria);

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
        List<ApartmentEntity> result = apartmentRepository.findApartmentsByCriteria(criteria);

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
        List<ApartmentEntity> result = apartmentRepository.findApartmentsByCriteria(criteria);

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
        List<ApartmentEntity> result = apartmentRepository.findApartmentsByCriteria(criteria);

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
        List<ApartmentEntity> result = apartmentRepository.findApartmentsByCriteria(criteria);

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