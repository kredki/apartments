package com.capgemini.service.impl;

import com.capgemini.dao.ApartmentRepository;
import com.capgemini.dao.BuildingRepository;
import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ApartmentEntity;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.testutils.ApartmentGenerator;
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
    private ApartmentGenerator apartmentGenerator;

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
                .price(new BigDecimal("120000"))
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