package com.capgemini.mappers;

import com.capgemini.dao.ApartmentRepository;
import com.capgemini.dao.BuildingRepository;
import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ApartmentEntity;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.types.AddressTO;
import com.capgemini.types.BuildingTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class BuildingMapperTest {
    @Autowired
    private BuildingMapper buildingMapper;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private ApartmentRepository apartmentRepository;

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
    }

    @Test
    public void shouldNotMapToTO() {
        //when
        BuildingTO mappedBuilding = buildingMapper.toTO(null);

        //then
        assertThat(mappedBuilding).isNull();
    }

    @Test
    public void shouldMapToTO() {
        //given
        BuildingEntity building = BuildingEntity.builder()
                .address(addressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        building = buildingRepository.save(building);

        Set<ApartmentEntity> apartments = new HashSet<>();
        ApartmentEntity apartment1 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building)
                .floor(0)
                .roomQty(4)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartment1 = apartmentRepository.save(apartment1);
        apartments.add(apartment1);

        ApartmentEntity apartment2 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building)
                .floor(0)
                .roomQty(4)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartment2 = apartmentRepository.save(apartment2);
        apartments.add(apartment2);
        building.setApartments(apartments);
        building = buildingRepository.save(building);

        //when
        BuildingTO mappedBuilding = buildingMapper.toTO(building);

        //then
        assertThat(mappedBuilding).isNotNull();
        assertThat(mappedBuilding.getId()).isEqualTo(building.getId());
        assertThat(mappedBuilding.getApartmentsQty()).isEqualTo(building.getApartmentsQty());
        assertThat(mappedBuilding.getDescription()).isEqualTo(building.getDescription());
        assertThat(mappedBuilding.getFloorQty()).isEqualTo(building.getFloorQty());
        assertThat(mappedBuilding.getIsElevatorPresent()).isEqualTo(building.getIsElevatorPresent());
        assertThat(mappedBuilding.getVersion()).isEqualTo(building.getVersion());
        Set<Long> mappedApartments = mappedBuilding.getApartments();
        assertThat(mappedApartments).isNotNull().isNotEmpty();
        assertThat(mappedApartments.size()).isEqualTo(2);
        for (ApartmentEntity apartment : apartments) {
            assertThat(mappedApartments.contains(apartment.getId())).isTrue();
        }
    }

    @Test
    public void shouldNotMapToEntity() {
        //when
        BuildingEntity mappedBuilding = buildingMapper.toEntity(null);

        //then
        assertThat(mappedBuilding).isNull();
    }

    @Test
    public void shouldMapToEntity() {
        //given
        BuildingEntity building = BuildingEntity.builder()
                .address(addressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        building = buildingRepository.save(building);

        Set<ApartmentEntity> apartments = new HashSet<>();
        ApartmentEntity apartment1 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building)
                .floor(0)
                .roomQty(4)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartment1 = apartmentRepository.save(apartment1);
        apartments.add(apartment1);

        ApartmentEntity apartment2 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building)
                .floor(0)
                .roomQty(4)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartment2 = apartmentRepository.save(apartment2);
        apartments.add(apartment2);
        building.setApartments(apartments);
        building = buildingRepository.save(building);

        BuildingTO buildingToMap = BuildingTO.builder()
                .address(addressTO)
                .apartmentsQty(building.getApartmentsQty())
                .description(building.getDescription())
                .floorQty(building.getFloorQty())
                .isElevatorPresent(building.getIsElevatorPresent())
                .apartments(building.getApartments().stream().map(x -> x.getId()).collect(Collectors.toSet()))
                .build();

        //when
        BuildingEntity mappedBuilding = buildingMapper.toEntity(buildingToMap);

        //then
        assertThat(mappedBuilding).isNotNull();
        assertThat(mappedBuilding.getId()).isEqualTo(buildingToMap.getId());
        assertThat(mappedBuilding.getApartmentsQty()).isEqualTo(buildingToMap.getApartmentsQty());
        assertThat(mappedBuilding.getDescription()).isEqualTo(buildingToMap.getDescription());
        assertThat(mappedBuilding.getFloorQty()).isEqualTo(buildingToMap.getFloorQty());
        assertThat(mappedBuilding.getIsElevatorPresent()).isEqualTo(buildingToMap.getIsElevatorPresent());
        assertThat(mappedBuilding.getVersion()).isEqualTo(buildingToMap.getVersion());
        Set<Long> mappedApartments = mappedBuilding.getApartments().stream().map(x -> x.getId()).collect(Collectors.toSet());
        assertThat(mappedApartments).isNotNull().isNotEmpty();
        assertThat(mappedApartments.size()).isEqualTo(2);
        for (ApartmentEntity apartment : apartments) {
            assertThat(mappedApartments.contains(apartment.getId())).isTrue();
        }
    }

    @Test
    public void shouldNotMap2TOs() {
        //when
        Set<BuildingTO> mappedBuilding = buildingMapper.map2TOs(new HashSet<>());

        //then
        assertThat(mappedBuilding).isNotNull().isEmpty();
    }

    @Test
    public void shouldMap2TOs() {
        //given
        BuildingEntity building1 = BuildingEntity.builder()
                .address(addressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        building1 = buildingRepository.save(building1);

        Set<ApartmentEntity> apartments1 = new HashSet<>();
        ApartmentEntity apartment1 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building1)
                .floor(0)
                .roomQty(4)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartment1 = apartmentRepository.save(apartment1);
        apartments1.add(apartment1);

        ApartmentEntity apartment2 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building1)
                .floor(0)
                .roomQty(4)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartment2 = apartmentRepository.save(apartment2);
        apartments1.add(apartment2);
        building1.setApartments(apartments1);
        building1 = buildingRepository.save(building1);

        BuildingEntity building2 = BuildingEntity.builder()
                .address(addressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        building2 = buildingRepository.save(building2);

        Set<ApartmentEntity> apartments2 = new HashSet<>();
        ApartmentEntity apartment3 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building2)
                .floor(0)
                .roomQty(4)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartment3 = apartmentRepository.save(apartment3);
        apartments2.add(apartment3);

        ApartmentEntity apartment4 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building2)
                .floor(0)
                .roomQty(4)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartment4 = apartmentRepository.save(apartment4);
        apartments2.add(apartment4);
        building2.setApartments(apartments2);
        building2 = buildingRepository.save(building2);

        Set<BuildingEntity> buildings = new HashSet<>();
        buildings.add(building1);
        buildings.add(building2);

        //when
        Set<BuildingTO> mappedBuildings = buildingMapper.map2TOs(buildings);

        //then
        assertThat(mappedBuildings).isNotNull().isNotEmpty();
        Set<Long> mappedBuildingsIds = mappedBuildings.stream().map(x -> x.getId()).collect(Collectors.toSet());
        for(BuildingTO building : mappedBuildings) {
            assertThat(mappedBuildingsIds.contains(building.getId())).isTrue();
        }
    }

    @Test
    public void shouldNotMap2Entities() {
        //when
        Set<BuildingEntity> mappedBuilding = buildingMapper.map2Entities(new HashSet<>());

        //then
        assertThat(mappedBuilding).isNotNull().isEmpty();
    }

    @Test
    public void shouldMap2Entities() {
        //given
        BuildingEntity building1 = BuildingEntity.builder()
                .address(addressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        building1 = buildingRepository.save(building1);

        Set<ApartmentEntity> apartments1 = new HashSet<>();
        ApartmentEntity apartment1 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building1)
                .floor(0)
                .roomQty(4)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartment1 = apartmentRepository.save(apartment1);
        apartments1.add(apartment1);

        ApartmentEntity apartment2 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building1)
                .floor(0)
                .roomQty(4)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartment2 = apartmentRepository.save(apartment2);
        apartments1.add(apartment2);
        building1.setApartments(apartments1);
        building1 = buildingRepository.save(building1);

        BuildingTO building1ToMap = BuildingTO.builder()
                .address(addressTO)
                .apartmentsQty(building1.getApartmentsQty())
                .description(building1.getDescription())
                .floorQty(building1.getFloorQty())
                .isElevatorPresent(building1.getIsElevatorPresent())
                .apartments(building1.getApartments().stream().map(x -> x.getId()).collect(Collectors.toSet()))
                .build();

        BuildingEntity building2 = BuildingEntity.builder()
                .address(addressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        building2 = buildingRepository.save(building2);

        Set<ApartmentEntity> apartments2 = new HashSet<>();
        ApartmentEntity apartment3 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building2)
                .floor(0)
                .roomQty(4)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartment3 = apartmentRepository.save(apartment3);
        apartments2.add(apartment3);

        ApartmentEntity apartment4 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building2)
                .floor(0)
                .roomQty(4)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartment4 = apartmentRepository.save(apartment4);
        apartments2.add(apartment4);
        building2.setApartments(apartments2);
        building2 = buildingRepository.save(building2);

        BuildingTO building2ToMap = BuildingTO.builder()
                .address(addressTO)
                .apartmentsQty(building2.getApartmentsQty())
                .description(building2.getDescription())
                .floorQty(building2.getFloorQty())
                .isElevatorPresent(building2.getIsElevatorPresent())
                .apartments(building2.getApartments().stream().map(x -> x.getId()).collect(Collectors.toSet()))
                .build();
        Set<BuildingTO> buildingsToMap = new HashSet<>();
        buildingsToMap.add(building1ToMap);
        buildingsToMap.add(building2ToMap);

        //when
        Set<BuildingEntity> mappedBuildings = buildingMapper.map2Entities(buildingsToMap);

        //then
        assertThat(mappedBuildings).isNotNull();
        Set<Long> mappedBuildingsIds = mappedBuildings.stream().map(x -> x.getId()).collect(Collectors.toSet());
        for(BuildingEntity building : mappedBuildings) {
            assertThat(mappedBuildingsIds.contains(building.getId())).isTrue();
        }
    }

    @Test
    public void shouldNotMap2TOs2() {
        //when
        List<BuildingTO> mappedBuilding = buildingMapper.map2TOs(new ArrayList<>());

        //then
        assertThat(mappedBuilding).isNotNull().isEmpty();
    }

    @Test
    public void shouldMap2TOs2() {
        //given
        BuildingEntity building1 = BuildingEntity.builder()
                .address(addressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        building1 = buildingRepository.save(building1);

        Set<ApartmentEntity> apartments1 = new HashSet<>();
        ApartmentEntity apartment1 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building1)
                .floor(0)
                .roomQty(4)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartment1 = apartmentRepository.save(apartment1);
        apartments1.add(apartment1);

        ApartmentEntity apartment2 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building1)
                .floor(0)
                .roomQty(4)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartment2 = apartmentRepository.save(apartment2);
        apartments1.add(apartment2);
        building1.setApartments(apartments1);
        building1 = buildingRepository.save(building1);

        BuildingEntity building2 = BuildingEntity.builder()
                .address(addressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        building2 = buildingRepository.save(building2);

        Set<ApartmentEntity> apartments2 = new HashSet<>();
        ApartmentEntity apartment3 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building2)
                .floor(0)
                .roomQty(4)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartment3 = apartmentRepository.save(apartment3);
        apartments2.add(apartment3);

        ApartmentEntity apartment4 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building2)
                .floor(0)
                .roomQty(4)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartment4 = apartmentRepository.save(apartment4);
        apartments2.add(apartment4);
        building2.setApartments(apartments2);
        building2 = buildingRepository.save(building2);

        List<BuildingEntity> buildings = new ArrayList<>();
        buildings.add(building1);
        buildings.add(building2);

        //when
        List<BuildingTO> mappedBuildings = buildingMapper.map2TOs(buildings);

        //then
        assertThat(mappedBuildings).isNotNull().isNotEmpty();
        Set<Long> mappedBuildingsIds = mappedBuildings.stream().map(x -> x.getId()).collect(Collectors.toSet());
        for(BuildingTO building : mappedBuildings) {
            assertThat(mappedBuildingsIds.contains(building.getId())).isTrue();
        }
    }

    @Test
    public void shouldNotMap2Entities2() {
        //when
        List<BuildingEntity> mappedBuilding = buildingMapper.map2Entities(new ArrayList<>());

        //then
        assertThat(mappedBuilding).isNotNull().isEmpty();
    }

    @Test
    public void shouldMap2Entities2() {
        //given
        BuildingEntity building1 = BuildingEntity.builder()
                .address(addressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        building1 = buildingRepository.save(building1);

        Set<ApartmentEntity> apartments1 = new HashSet<>();
        ApartmentEntity apartment1 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building1)
                .floor(0)
                .roomQty(4)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartment1 = apartmentRepository.save(apartment1);
        apartments1.add(apartment1);

        ApartmentEntity apartment2 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building1)
                .floor(0)
                .roomQty(4)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartment2 = apartmentRepository.save(apartment2);
        apartments1.add(apartment2);
        building1.setApartments(apartments1);
        building1 = buildingRepository.save(building1);

        BuildingTO building1ToMap = BuildingTO.builder()
                .address(addressTO)
                .apartmentsQty(building1.getApartmentsQty())
                .description(building1.getDescription())
                .floorQty(building1.getFloorQty())
                .isElevatorPresent(building1.getIsElevatorPresent())
                .apartments(building1.getApartments().stream().map(x -> x.getId()).collect(Collectors.toSet()))
                .build();

        BuildingEntity building2 = BuildingEntity.builder()
                .address(addressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        building2 = buildingRepository.save(building2);

        Set<ApartmentEntity> apartments2 = new HashSet<>();
        ApartmentEntity apartment3 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building2)
                .floor(0)
                .roomQty(4)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartment3 = apartmentRepository.save(apartment3);
        apartments2.add(apartment3);

        ApartmentEntity apartment4 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building2)
                .floor(0)
                .roomQty(4)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartment4 = apartmentRepository.save(apartment4);
        apartments2.add(apartment4);
        building2.setApartments(apartments2);
        building2 = buildingRepository.save(building2);

        BuildingTO building2ToMap = BuildingTO.builder()
                .address(addressTO)
                .apartmentsQty(building2.getApartmentsQty())
                .description(building2.getDescription())
                .floorQty(building2.getFloorQty())
                .isElevatorPresent(building2.getIsElevatorPresent())
                .apartments(building2.getApartments().stream().map(x -> x.getId()).collect(Collectors.toSet()))
                .build();
        List<BuildingTO> buildingsToMap = new ArrayList<>();
        buildingsToMap.add(building1ToMap);
        buildingsToMap.add(building2ToMap);

        //when
        List<BuildingEntity> mappedBuildings = buildingMapper.map2Entities(buildingsToMap);

        //then
        assertThat(mappedBuildings).isNotNull();
        Set<Long> mappedBuildingsIds = mappedBuildings.stream().map(x -> x.getId()).collect(Collectors.toSet());
        for(BuildingEntity building : mappedBuildings) {
            assertThat(mappedBuildingsIds.contains(building.getId())).isTrue();
        }
    }
}