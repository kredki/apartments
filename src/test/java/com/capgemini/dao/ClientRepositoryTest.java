package com.capgemini.dao;

import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ApartmentEntity;
import com.capgemini.domain.ClientEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ClientRepositoryTest {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ApartmentRepository apartmentRepository;

    private AddressInTable address;
    private ClientEntity client1;

    @Before
    public void setup() {
        address = AddressInTable.builder()
                .city("city")
                .no("no")
                .postalCode("code")
                .street("street")
                .build();
        client1 = ClientEntity.builder()
                .telephone("tel1")
                .lastName("Iksinski")
                .firstName("Andrzej")
                .address(address)
                .build();

        BigDecimal price1 = new BigDecimal("123000.12");
        BigDecimal area1 = new BigDecimal("12.12");
        Set<ApartmentEntity> apartments = new HashSet<>();

        ApartmentEntity apartment1 = ApartmentEntity.builder()
                .balconyQty(1)
                .floor(0)
                .status("sold")
                .price(price1)
                .roomQty(3)
                .area(area1)
                .address(address)
                //.owners(Collections.singleton(client1))
                //.mainOwner(client1)
                .build();
        apartments.add(apartment1);

        ApartmentEntity apartment2 = ApartmentEntity.builder()
                .balconyQty(2)
                .floor(0)
                .status("free")
                .price(price1)
                .roomQty(2)
                .area(area1)
                .address(address)
                //.owners(Collections.singleton(client1))
                //.mainOwner(client1)
                .build();

        ApartmentEntity apartment3 = ApartmentEntity.builder()
                .balconyQty(2)
                .floor(0)
                .status("sold")
                .price(price1)
                .roomQty(2)
                .area(area1)
                .address(address)
                //.owners(Collections.singleton(client1))
                //.mainOwner(client1)
                .build();
        apartments.add(apartment2);
        client1 = clientRepository.save(client1);
        apartmentRepository.save(apartment1);
        apartmentRepository.save(apartment2);
        apartmentRepository.save(apartment3);/*

        apartment1.setMainOwner(client1);
        apartment2.setMainOwner(client1);
        apartment3.setMainOwner(client1);
        apartment1.setOwners(Collections.singleton(client1));
        apartment2.setOwners(Collections.singleton(client1));
        apartment3.setOwners(Collections.singleton(client1));*/
        apartmentRepository.save(apartment1);
        apartmentRepository.save(apartment2);
        apartmentRepository.save(apartment3);

        client1.setApartments(apartments);
        client1 = clientRepository.save(client1);
    }

    @Test
    public void shouldFindApartmentsWorthForClient() {
        //
        BigDecimal expectedPriceSum = new BigDecimal("246000.24");

        //when
        BigDecimal priceSum = clientRepository.findApartmentsWorthForClient(client1.getId());

        //then
        assertEquals(expectedPriceSum, priceSum);
    }

    @Test
    public void shouldAddClient() {
        //given
        long clientsQty = clientRepository.count();
        ClientEntity client = ClientEntity.builder()
                .telephone("tel")
                .lastName("Nowak")
                .firstName("Jan")
                .address(address)
                .build();

        //when
        ClientEntity savedClient = clientRepository.save(client);

        //then
        assertEquals(clientsQty + 1, clientRepository.count());
        assertEquals(client.getFirstName(), savedClient.getFirstName());
        assertEquals(client.getLastName(), savedClient.getLastName());
        assertEquals(client.getTelephone(), savedClient.getTelephone());
    }

    @Test
    public void shouldUseOptimisticLocking() {
        //given
        long versionBefore = client1.getVersion();
        long clientsQtyBefore = clientRepository.count();
        long client1Id = client1.getId();
        ClientEntity clientA = ClientEntity.builder()
                .id(client1Id)
                .telephone("tel2")
                .lastName("Kowalski")
                .firstName("Stefan")
                .address(address)
                .version(versionBefore)
                .build();
        ClientEntity clientB = ClientEntity.builder()
                .id(client1Id)
                .telephone("tel3")
                .lastName("Kowalski2")
                .firstName("Stefan2")
                .address(address)
                .version(versionBefore)
                .build();

        //when
        clientRepository.save(clientA);
        try {
            clientRepository.save(clientB);
            fail();
        } catch (ObjectOptimisticLockingFailureException e) {
        }

        //then
        ClientEntity savedClient = clientRepository.findOne(client1Id);
        assertEquals(clientsQtyBefore, clientRepository.count());
        assertEquals(clientA.getLastName(), savedClient.getLastName());
        assertEquals(clientA.getTelephone(), savedClient.getTelephone());
        assertEquals(clientA.getFirstName(), savedClient.getFirstName());
        assertEquals(new Long(versionBefore + 1L), savedClient.getVersion());
    }
}