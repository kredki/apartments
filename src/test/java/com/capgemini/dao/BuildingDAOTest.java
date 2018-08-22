package com.capgemini.dao;

import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.BuildingEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
public class BuildingDAOTest {
    @Autowired
    BuildingDAO buildingDAO;

    @Before
    public void setup() {

    }

    @Test
    public void shouldAddBuilding() {
        //given
        long clientsQty = buildingDAO.count();
        AddressInTable address = new AddressInTable.Builder().withCity("city").withNo("no").withPostalCode("code")
                .withStreet("street").build();
        BuildingEntity building = BuildingEntity.builder().address(address).apartmentsQty(0).description("description")
                .floorQty(0).isElevatorPresent(true).build();

        //when
        BuildingEntity savedBuilding = buildingDAO.save(building);

        //then
        assertEquals(clientsQty + 1, buildingDAO.count());
        assertEquals(building.getApartmentsQty(), savedBuilding.getApartmentsQty());
        assertEquals(building.getDescription(), savedBuilding.getDescription());
        assertEquals(building.getFloorQty(), savedBuilding.getApartmentsQty());
        assertEquals(building.getIsElevatorPresent(), savedBuilding.getIsElevatorPresent());
    }
}