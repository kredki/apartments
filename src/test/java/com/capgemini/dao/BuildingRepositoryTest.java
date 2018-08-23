package com.capgemini.dao;

import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.BuildingEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BuildingRepositoryTest {
    @Autowired
    BuildingRepository buildingRepository;

    @Before
    public void setup() {

    }

    @Test
    public void shouldAddBuilding() {
        //given
        long clientsQty = buildingRepository.count();
        AddressInTable address = AddressInTable.builder().city("city").no("no").postalCode("code").street("street").build();
        BuildingEntity building = BuildingEntity.builder().address(address).apartmentsQty(0).description("description")
                .floorQty(0).isElevatorPresent(true).build();

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