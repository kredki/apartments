package com.capgemini.dao;

import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ApartmentEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
public class ApartmentDAOTest {
    @Autowired
    ApartmentDAO apartmentDAO;

    @Before
    public void setup() {

    }

    @Test
    public void shouldAddBuilding() {
        //given
        long clientsQty = apartmentDAO.count();
        AddressInTable address = new AddressInTable.Builder().withCity("city").withNo("no").withPostalCode("code")
                .withStreet("street").build();
        ApartmentEntity apertment = ApartmentEntity.builder().address(address).area(new BigDecimal("5000"))
                .roomQty(10).price(new BigDecimal("1.29")).status("FREE").floor(0).balconyQty(12).build();

        //when
        ApartmentEntity savedApartment = apartmentDAO.save(apertment);

        //then
        assertEquals(clientsQty + 1, apartmentDAO.count());
        assertEquals(apertment.getArea(), savedApartment.getArea());
        assertEquals(apertment.getBalconyQty(), savedApartment.getBalconyQty());
        assertEquals(apertment.getFloor(), savedApartment.getFloor());
        assertEquals(apertment.getPrice(), savedApartment.getPrice());
    }
}