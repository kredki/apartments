package com.capgemini.service.impl;

import com.capgemini.dao.ApartmentRepository;
import com.capgemini.dao.BuildingRepository;
import com.capgemini.dao.ClientRepository;
import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ApartmentEntity;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.domain.ClientEntity;
import com.capgemini.testutils.ApartmentGenerator;
import com.capgemini.testutils.BuildingGenerator;
import com.capgemini.testutils.ClientGenerator;
import com.capgemini.types.AddressTO;
import com.capgemini.types.ApartmentTO;
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
public class ApartmentServiceImplTest {
    @Autowired
    private ApartmentServiceImpl apartmentService;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ApartmentGenerator apartmentGenerator;
    @Autowired
    private ClientGenerator clientGenerator;
    @Autowired
    private BuildingGenerator buildingGenerator;

    private BuildingEntity building;
    private AddressTO addressTO;
    private AddressInTable addressInTable;

    @Before
    public void setup() {
        addressTO = AddressTO.builder()
                .street("street")
                .postalCode("code")
                .no("no").city("city")
                .build();
        addressInTable = AddressInTable.builder()
                .street("street")
                .postalCode("code")
                .no("no").city("city")
                .build();
        building = BuildingEntity.builder()
                .address(addressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        building = buildingRepository.save(building);
    }

    @Test
    @Transactional
    public void shouldFindById() {
        //given
        ApartmentEntity apartment = apartmentGenerator.getFreeApartment();
        apartment = apartmentRepository.save(apartment);

        //then
        ApartmentTO foundApartment = apartmentService.findById(apartment.getId());

        //then
        assertThat(foundApartment).isNotNull();
        assertThat(foundApartment.getId()).isEqualTo(apartment.getId());
        assertThat(foundApartment.getArea()).isEqualTo(apartment.getArea());
        assertThat(foundApartment.getBalconyQty()).isEqualTo(apartment.getBalconyQty());
        assertThat(foundApartment.getFloor()).isEqualTo(apartment.getFloor());
        assertThat(foundApartment.getPrice()).isEqualTo(apartment.getPrice());
        assertThat(foundApartment.getRoomQty()).isEqualTo(apartment.getRoomQty());
        assertThat(foundApartment.getStatus()).isEqualTo(apartment.getStatus());
    }

    @Test
    public void shouldFindAll() {
        //given
        long apartmentsQtyBefore = apartmentRepository.count();
        List<ApartmentEntity> apartmentsBefore = apartmentRepository.findAll();
        List<Long> idsBefore = apartmentsBefore.stream().map(x -> x.getId()).collect(Collectors.toList());

        //when
        List<ApartmentTO> foundApartments = apartmentService.findAll();

        //then
        assertEquals(apartmentsQtyBefore, foundApartments.size());
        List<Long> idsAfter = foundApartments.stream().map(x -> x.getId()).collect(Collectors.toList());
        for (Long id : idsBefore) {
            assertTrue(idsAfter.contains(id));
        }

    }

    @Test
    public void shouldNotAddApartment() {
        //given
        long apartmentsQtyBefore = apartmentRepository.count();

        //when
        ApartmentTO apartment = apartmentService.addNewApartment(null, building.getId());

        //then
        assertThat(apartment).isNull();
        assertThat(apartmentRepository.count()).isEqualTo(apartmentsQtyBefore);
    }

    @Test
    public void shouldNotAddApartment2() {
        //given
        long apartmentsQtyBefore = apartmentRepository.count();
        ApartmentTO apartmentToAdd = ApartmentTO.builder()
                .address(addressTO)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building.getId())
                .floor(0)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();

        //when
        ApartmentTO apartment = apartmentService.addNewApartment(apartmentToAdd, null);

        //then
        assertThat(apartment).isNull();
        assertThat(apartmentRepository.count()).isEqualTo(apartmentsQtyBefore);
    }

    @Test
    public void shouldNotRemoveApartment() {
        //given
        Long apartmentQtyBefore = apartmentRepository.count();
        List<ApartmentEntity> apartmentsBefore = apartmentRepository.findAll();
        List<Long> idsBefore = apartmentsBefore.stream().map(x -> x.getId()).collect(Collectors.toList());
        Optional<Long> max = idsBefore.stream().max(Comparator.naturalOrder());
        Long idNotInDB = 1L;
        if(max.isPresent()) {
            idNotInDB = max.get() + 1;
        }

        //when
        apartmentService.removeApartment(idNotInDB);

        //then
        assertThat(apartmentRepository.count()).isEqualTo(apartmentQtyBefore);
        List<ApartmentEntity> apartmentsAfter = apartmentRepository.findAll();
        List<Long> idsAfter = apartmentsAfter.stream().map(x -> x.getId()).collect(Collectors.toList());
        for (Long id : idsBefore) {
            assertTrue(idsAfter.contains(id));
        }
    }

    @Test
    @Transactional
    public void shouldRemoveApartment() {
        //given
        List<ApartmentEntity> apartmentsBefore = apartmentRepository.findAll();
        List<Long> idsBefore = apartmentsBefore.stream().map(x -> x.getId()).collect(Collectors.toList());
        ApartmentEntity apartmentBefore = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building)
                .floor(0)
                .roomQty(1)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartmentBefore = apartmentRepository.save(apartmentBefore);
        long apartmentQtyBefore = apartmentRepository.count();

        //when
        apartmentService.removeApartment(apartmentBefore.getId());

        //then
        assertThat(apartmentRepository.count()).isEqualTo(apartmentQtyBefore - 1);
        List<ApartmentEntity> apartmentsAfter = apartmentRepository.findAll();
        List<Long> idsAfter = apartmentsAfter.stream().map(x -> x.getId()).collect(Collectors.toList());
        for (Long id : idsBefore) {
            assertTrue(idsAfter.contains(id));
        }
    }

    @Test
    @Transactional
    public void shouldUpdateApartment() {
        //given
        ApartmentEntity apartmentBefore = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building)
                .floor(0)
                .roomQty(2)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartmentBefore = apartmentRepository.save(apartmentBefore);
        long apartmentQtyBefore = apartmentRepository.count();

        ApartmentTO apartmentToUpdate = ApartmentTO.builder()
                .id(apartmentBefore.getId())
                .address(addressTO)
                .area(new BigDecimal("70"))
                .balconyQty(12)
                .building(building.getId())
                .floor(10)
                .roomQty(1)
                .price(new BigDecimal("1200000"))
                .version(apartmentBefore.getVersion())
                .status("sold")
                .build();

        //when
        ApartmentTO result = apartmentService.updateApartment(apartmentToUpdate);

        //then
        assertThat(result).isNotNull();
        assertThat(apartmentRepository.count()).isEqualTo(apartmentQtyBefore);
        assertThat(result.getId()).isEqualTo(apartmentToUpdate.getId());
        assertThat(result.getArea()).isEqualTo(apartmentToUpdate.getArea());
        assertThat(result.getBalconyQty()).isEqualTo(apartmentToUpdate.getBalconyQty());
        assertThat(result.getFloor()).isEqualTo(apartmentToUpdate.getFloor());
        assertThat(result.getPrice()).isEqualTo(apartmentToUpdate.getPrice());
        assertThat(result.getRoomQty()).isEqualTo(apartmentToUpdate.getRoomQty());
        assertThat(result.getStatus()).isEqualTo(apartmentToUpdate.getStatus());

        ApartmentEntity apartmentAfter = apartmentRepository.findOne(apartmentBefore.getId());
        assertThat(apartmentAfter.getId()).isEqualTo(apartmentToUpdate.getId());
        assertThat(apartmentAfter.getArea()).isEqualTo(apartmentToUpdate.getArea());
        assertThat(apartmentAfter.getBalconyQty()).isEqualTo(apartmentToUpdate.getBalconyQty());
        assertThat(apartmentAfter.getFloor()).isEqualTo(apartmentToUpdate.getFloor());
        assertThat(apartmentAfter.getPrice()).isEqualTo(apartmentToUpdate.getPrice());
        assertThat(apartmentAfter.getRoomQty()).isEqualTo(apartmentToUpdate.getRoomQty());
        assertThat(apartmentAfter.getStatus()).isEqualTo(apartmentToUpdate.getStatus());
    }

    @Test
    @Transactional
    public void shouldAddApartment() {
        //given
        Set<ApartmentEntity>apartments = building.getApartments();
        Integer apartmentsQtyBefore = apartments.size();

        ApartmentTO apartmentToAdd = ApartmentTO.builder()
                .address(addressTO)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building.getId())
                .floor(0)
                .roomQty(1)
                .price(new BigDecimal("12000012"))
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

    @Test
    @Transactional
    public void shouldAddReservation() {
        //given
        ClientEntity client = clientGenerator.getClient();
        ApartmentEntity apartment = apartmentGenerator.getFreeApartment();
        BuildingEntity building = buildingGenerator.getBuilding();
        apartment.setBuilding(building);
        client = clientRepository.save(client);
        apartment = apartmentRepository.save(apartment);
        Long apartmentQtyBefore = apartmentRepository.count();
        long clientQtyBefore = clientRepository.count();
        Long clientId = client.getId();
        Long apartmentId = apartment.getId();

        //when
        boolean reservationAdded = apartmentService.addReservation(clientId, apartmentId);

        //then
        assertTrue(reservationAdded);
        assertThat(apartmentRepository.count()).isEqualTo(apartmentQtyBefore);
        assertThat(clientRepository.count()).isEqualTo(clientQtyBefore);
        ApartmentEntity apartmentAfter = apartmentRepository.findOne(apartmentId);
        ClientEntity clientAfter = clientRepository.findOne(clientId);
        assertThat(apartmentAfter.getStatus().toLowerCase()).isEqualTo("reserved");
        ClientEntity mainOwner = apartmentAfter.getMainOwner();
        assertThat(mainOwner).isNotNull();
        assertThat(mainOwner.getId()).isEqualTo(clientId);
        Set<ClientEntity> owners = apartmentAfter.getOwners();
        assertThat(owners).isNotNull().isNotEmpty();
        List<Long> ownersIds = owners.stream().map(x -> x.getId()).collect(Collectors.toList());
        assertTrue(ownersIds.contains(clientId));
        Set<ApartmentEntity> apartments = clientAfter.getApartments();
        assertThat(apartments).isNotNull().isNotEmpty();
        assertThat(apartments.size()).isEqualTo(1);
        assertThat(apartments.iterator().next().getId()).isEqualTo(apartmentId);
    }

    @Test
    @Transactional
    public void shouldNotAddReservation() {
        //given
        ClientEntity client = clientGenerator.getClient();
        ApartmentEntity apartmentToReserve = apartmentGenerator.getFreeApartment();
        BuildingEntity building = buildingGenerator.getBuilding();
        apartmentToReserve.setBuilding(building);
        apartmentToReserve = apartmentRepository.save(apartmentToReserve);
        ApartmentEntity apartment1 = apartmentGenerator.getReservedApartment();
        ApartmentEntity apartment2 = apartmentGenerator.getReservedApartment();
        ApartmentEntity apartment3 = apartmentGenerator.getReservedApartment();
        apartment1.setBuilding(building);
        apartment2.setBuilding(building);
        apartment3.setBuilding(building);

        client = clientRepository.save(client);
        apartment1 = apartmentRepository.save(apartment1);
        apartment2 = apartmentRepository.save(apartment2);
        apartment3 = apartmentRepository.save(apartment3);

        Set<ApartmentEntity> apartments = new HashSet<>();
        apartments.add(apartment1);
        apartments.add(apartment2);
        apartments.add(apartment3);
        Set<ClientEntity> owners = new HashSet<>();
        owners.add(client);

        apartment1.setMainOwner(client);
        apartment2.setMainOwner(client);
        apartment3.setMainOwner(client);
        apartment1.setOwners(owners);
        apartment2.setOwners(owners);
        apartment3.setOwners(owners);
        client.setApartments(apartments);

        client = clientRepository.save(client);
        apartment1 = apartmentRepository.save(apartment1);
        apartment2 = apartmentRepository.save(apartment2);
        apartment3 = apartmentRepository.save(apartment3);

        Long apartmentQtyBefore = apartmentRepository.count();
        long clientQtyBefore = clientRepository.count();
        Long clientId = client.getId();
        Long apartmentToReserveId = apartmentToReserve.getId();

        //when
        boolean reservationAdded = apartmentService.addReservation(clientId, apartmentToReserveId);

        //then
        assertFalse(reservationAdded);
        assertThat(apartmentRepository.count()).isEqualTo(apartmentQtyBefore);
        assertThat(clientRepository.count()).isEqualTo(clientQtyBefore);
        ApartmentEntity apartment1After = apartmentRepository.findOne(apartment1.getId());
        ApartmentEntity apartment2After = apartmentRepository.findOne(apartment2.getId());
        ApartmentEntity apartment3After = apartmentRepository.findOne(apartment3.getId());
        ClientEntity clientAfter = clientRepository.findOne(clientId);

        assertThat(apartment1After.getStatus().toLowerCase()).isEqualTo("reserved");
        assertThat(apartment2After.getStatus().toLowerCase()).isEqualTo("reserved");
        assertThat(apartment3After.getStatus().toLowerCase()).isEqualTo("reserved");

        ClientEntity mainOwner1 = apartment1After.getMainOwner();
        assertThat(mainOwner1).isNotNull();
        Set<ClientEntity> apartment1AfterOwners = apartment1After.getOwners();
        assertThat(apartment1AfterOwners).isNotNull().isNotEmpty();

        ClientEntity mainOwner2 = apartment1After.getMainOwner();
        assertThat(mainOwner2).isNotNull();
        Set<ClientEntity> apartment2AfterOwners = apartment1After.getOwners();
        assertThat(apartment2AfterOwners).isNotNull().isNotEmpty();

        ClientEntity mainOwner3 = apartment1After.getMainOwner();
        assertThat(mainOwner3).isNotNull();
        Set<ClientEntity> apartment3AfterOwners = apartment1After.getOwners();
        assertThat(apartment3AfterOwners).isNotNull().isNotEmpty();

        Set<ApartmentEntity> clientAfterApartments = clientAfter.getApartments();
        assertThat(clientAfterApartments).isNotNull();

        List<Long> afterApartmentsIds = clientAfterApartments.stream().map(x -> x.getId()).collect(Collectors.toList());
        assertTrue(afterApartmentsIds.contains(apartment1.getId()));
        assertTrue(afterApartmentsIds.contains(apartment2.getId()));
        assertTrue(afterApartmentsIds.contains(apartment3.getId()));
        assertFalse(afterApartmentsIds.contains(apartmentToReserve.getId()));
    }

    @Test
    @Transactional
    public void shouldNotAddReservationWhenApartmentIdNull() {
        //given
        ClientEntity client = clientGenerator.getClient();
        client = clientRepository.save(client);
        Long apartmentQtyBefore = apartmentRepository.count();
        long clientQtyBefore = clientRepository.count();
        Long clientId = client.getId();

        //when
        boolean reservationAdded = apartmentService.addReservation(clientId, null);

        //then
        assertFalse(reservationAdded);
        assertThat(apartmentRepository.count()).isEqualTo(apartmentQtyBefore);
        assertThat(clientRepository.count()).isEqualTo(clientQtyBefore);

        ClientEntity clientAfter = clientRepository.findOne(clientId);
        Set<ApartmentEntity> clientAfterApartments = clientAfter.getApartments();
        assertThat(clientAfterApartments).isEmpty();
    }

    @Test
    @Transactional
    public void shouldNotAddReservationWhenClientIdNull() {
        //given
        ApartmentEntity apartmentToReserve = apartmentGenerator.getFreeApartment();
        BuildingEntity building = buildingGenerator.getBuilding();
        apartmentToReserve.setBuilding(building);
        apartmentToReserve = apartmentRepository.save(apartmentToReserve);
        Long apartmentQtyBefore = apartmentRepository.count();
        long clientQtyBefore = clientRepository.count();
        Long apartmentToReserveId = apartmentToReserve.getId();

        //when
        boolean reservationAdded = apartmentService.addReservation(null, apartmentToReserveId);

        //then
        assertFalse(reservationAdded);
        assertThat(apartmentRepository.count()).isEqualTo(apartmentQtyBefore);
        assertThat(clientRepository.count()).isEqualTo(clientQtyBefore);
        ApartmentEntity apartmentAfter = apartmentRepository.findOne(apartmentToReserve.getId());
        assertThat(apartmentAfter.getMainOwner()).isNull();;
        assertThat(apartmentAfter.getOwners()).isEmpty();;
    }

    @Test
    public void shouldNotAddReservationWhenClientIdAndApartmentIdNull() {
        //given
        Long apartmentQtyBefore = apartmentRepository.count();
        long clientQtyBefore = clientRepository.count();

        //when
        boolean reservationAdded = apartmentService.addReservation(null, null);

        //then
        assertFalse(reservationAdded);
        assertThat(apartmentRepository.count()).isEqualTo(apartmentQtyBefore);
        assertThat(clientRepository.count()).isEqualTo(clientQtyBefore);
    }
}