package com.capgemini.service.impl;

import com.capgemini.dao.BuildingRepository;
import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.exceptions.VersionIsNullException;
import com.capgemini.types.AddressTO;
import com.capgemini.types.BuildingTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BuildingServiceImplTest {
    @Autowired
    BuildingRepository buildingRepository;
    @Autowired
    BuildingServiceImpl buildingService;

    private AddressTO addressTO;
    private AddressInTable AddressInTable;

    @Before
    public void setup() {
        addressTO = AddressTO.builder()
                .street("street")
                .postalCode("code")
                .no("no").city("city")
                .build();
        AddressInTable = AddressInTable.builder()
                .street("street")
                .postalCode("code")
                .no("no").city("city")
                .build();
    }

    @Test
    public void shouldAddBuilding() {
        //given
        BuildingTO buildingToAdd = BuildingTO.builder()
                .address(addressTO)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        long buildingQtyBefore = buildingRepository.count();

        //when
        BuildingTO addedBuilding = buildingService.addNewBuilding(buildingToAdd);

        //then
        assertThat(addedBuilding).isNotNull();
        assertThat(buildingRepository.count()).isEqualTo(buildingQtyBefore + 1);
        assertThat(addedBuilding.getIsElevatorPresent()).isEqualTo(buildingToAdd.getIsElevatorPresent());
        assertThat(addedBuilding.getApartmentsQty()).isEqualTo(buildingToAdd.getApartmentsQty());
        assertThat(addedBuilding.getDescription()).isEqualTo(buildingToAdd.getDescription());
        assertThat(addedBuilding.getFloorQty()).isEqualTo(buildingToAdd.getFloorQty());
        Long addedBuildingId = addedBuilding.getId();
        BuildingEntity addedBuildingFromDB = buildingRepository.findOne(addedBuildingId);
        assertThat(addedBuildingFromDB).isNotNull();
        assertThat(addedBuildingFromDB.getIsElevatorPresent()).isEqualTo(buildingToAdd.getIsElevatorPresent());
        assertThat(addedBuildingFromDB.getApartmentsQty()).isEqualTo(buildingToAdd.getApartmentsQty());
        assertThat(addedBuildingFromDB.getDescription()).isEqualTo(buildingToAdd.getDescription());
        assertThat(addedBuildingFromDB.getFloorQty()).isEqualTo(buildingToAdd.getFloorQty());
    }

    @Test
    public void shouldUpdateBuilding() {
        //given
        BuildingEntity building = BuildingEntity.builder()
                .address(AddressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        building = buildingRepository.save(building);

        BuildingTO buildingToUpdate = BuildingTO.builder()
                .id(building.getId())
                .address(addressTO)
                .apartmentsQty(0)
                .description("dsc4")
                .floorQty(53)
                .isElevatorPresent(true)
                .apartments(new HashSet<>())
                .version(building.getVersion())
                .build();
        long buildingQtyBefore = buildingRepository.count();

        //when
        BuildingTO addedBuilding = buildingService.updateBuilding(buildingToUpdate);

        //then
        assertThat(addedBuilding).isNotNull();
        assertThat(buildingRepository.count()).isEqualTo(buildingQtyBefore);
        assertThat(addedBuilding.getIsElevatorPresent()).isEqualTo(buildingToUpdate.getIsElevatorPresent());
        assertThat(addedBuilding.getApartmentsQty()).isEqualTo(buildingToUpdate.getApartmentsQty());
        assertThat(addedBuilding.getDescription()).isEqualTo(buildingToUpdate.getDescription());
        assertThat(addedBuilding.getFloorQty()).isEqualTo(buildingToUpdate.getFloorQty());

        BuildingEntity updatedBuildingFromDB = buildingRepository.findOne(building.getId());
        assertThat(updatedBuildingFromDB).isNotNull();
        assertThat(updatedBuildingFromDB.getIsElevatorPresent()).isEqualTo(buildingToUpdate.getIsElevatorPresent());
        assertThat(updatedBuildingFromDB.getApartmentsQty()).isEqualTo(buildingToUpdate.getApartmentsQty());
        assertThat(updatedBuildingFromDB.getDescription()).isEqualTo(buildingToUpdate.getDescription());
        assertThat(updatedBuildingFromDB.getFloorQty()).isEqualTo(buildingToUpdate.getFloorQty());
    }

    @Test(expected = VersionIsNullException.class)
    public void shouldNotUpdateBuilding() {
        //given
        BuildingEntity building = BuildingEntity.builder()
                .address(AddressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        building = buildingRepository.save(building);

        BuildingTO buildingToUpdate = BuildingTO.builder()
                .id(building.getId())
                .address(addressTO)
                .apartmentsQty(0)
                .description("dsc4")
                .floorQty(53)
                .isElevatorPresent(true)
                .apartments(new HashSet<>())
                .build();
        long buildingQtyBefore = buildingRepository.count();

        //when
        BuildingTO addedBuilding = buildingService.updateBuilding(buildingToUpdate);
    }

    @Test
    public void shouldRemoveBuilding() {
        //given
        BuildingEntity building = BuildingEntity.builder()
                .address(AddressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        building = buildingRepository.save(building);

        BuildingEntity buildingToRemove = BuildingEntity.builder()
                .address(AddressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        buildingToRemove = buildingRepository.save(buildingToRemove);

        long buildingQtyBefore = buildingRepository.count();

        //when
        buildingService.removeBuilding(buildingToRemove.getId());

        //then
        assertThat(buildingRepository.count()).isEqualTo(buildingQtyBefore - 1);
        List<BuildingEntity> buildingsAfterRemove = buildingRepository.findAll();
        Set<Long> buildingsAfterRemoveIds = buildingsAfterRemove.stream().map(x -> x.getId()).collect(Collectors.toSet());
        assertThat(buildingsAfterRemoveIds.contains(building.getId())).isTrue();
        assertThat(buildingsAfterRemoveIds.contains(buildingToRemove.getId())).isFalse();
    }

    @Test
    public void shouldFindAll() {
        //given
        BuildingEntity building1 = BuildingEntity.builder()
                .address(AddressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        building1 = buildingRepository.save(building1);

        BuildingEntity building2 = BuildingEntity.builder()
                .address(AddressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        building2 = buildingRepository.save(building2);
        List<BuildingEntity> savedBuildings = new ArrayList<>();
        savedBuildings.add(building1);
        savedBuildings.add(building2);

        //when
        List<BuildingTO> foundBuildings = buildingService.findAll();

        //then
        assertThat(foundBuildings).isNotNull().isNotEmpty();
        Set<Long> foundBuildingIds = foundBuildings.stream().map(x -> x.getId()).collect(Collectors.toSet());
        assertThat(foundBuildingIds.contains(building1.getId())).isTrue();
        assertThat(foundBuildingIds.contains(building2.getId())).isTrue();
        for (BuildingEntity savedBuilding : savedBuildings) {
            Long id = savedBuilding.getId();
            for (BuildingTO foundBuilding : foundBuildings) {
                assertThat(foundBuilding.getIsElevatorPresent()).isEqualTo(savedBuilding.getIsElevatorPresent());
                assertThat(foundBuilding.getApartmentsQty()).isEqualTo(savedBuilding.getApartmentsQty());
                assertThat(foundBuilding.getDescription()).isEqualTo(savedBuilding.getDescription());
                assertThat(foundBuilding.getFloorQty()).isEqualTo(savedBuilding.getFloorQty());
            }
        }
    }

    @Test
    public void shouldFindById() {
        //given
        BuildingEntity building = BuildingEntity.builder()
                .address(AddressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        building = buildingRepository.save(building);

        BuildingEntity buildingToFind = BuildingEntity.builder()
                .address(AddressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        buildingToFind = buildingRepository.save(buildingToFind);

        //when
        BuildingTO foundBuilding = buildingService.findById(buildingToFind.getId());

        //then
        assertThat(foundBuilding).isNotNull();

        assertThat(foundBuilding.getIsElevatorPresent()).isEqualTo(buildingToFind.getIsElevatorPresent());
        assertThat(foundBuilding.getApartmentsQty()).isEqualTo(buildingToFind.getApartmentsQty());
        assertThat(foundBuilding.getDescription()).isEqualTo(buildingToFind.getDescription());
        assertThat(foundBuilding.getFloorQty()).isEqualTo(buildingToFind.getFloorQty());
        assertThat(foundBuilding.getId()).isEqualTo(buildingToFind.getId());
    }
}