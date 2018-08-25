package com.capgemini.dao;

import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ApartmentEntity;
import com.capgemini.domain.ClientEntity;
import com.capgemini.testutils.ApartmentGenerator;
import com.capgemini.testutils.ClientGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class ClientRepositoryTest {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ApartmentRepository apartmentRepository;
    @Autowired
    ApartmentGenerator apartmentGenerator;
    @Autowired
    ClientGenerator clientGenerator;
    @PersistenceContext
    EntityManager entityManager;

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

        client1 = clientRepository.save(client1);

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
        apartment1 = apartmentRepository.save(apartment1);
        apartment2 = apartmentRepository.save(apartment2);
        apartment3 = apartmentRepository.save(apartment3);

        Set<ClientEntity> owners = new HashSet<>();
        owners.add(client1);

        apartment1.setMainOwner(client1);
        apartment2.setMainOwner(client1);
        apartment3.setMainOwner(client1);
        apartment1.setOwners(owners);
        apartment2.setOwners(owners);
        apartment3.setOwners(owners);
        apartmentRepository.save(apartment1);
        apartmentRepository.save(apartment2);
        apartmentRepository.save(apartment3);

        client1.setApartments(apartments);
        client1 = clientRepository.save(client1);
    }

    @Test
    @Transactional
    public void shouldFindClientsWithMoreThan1Apartment() {
        //given
        ClientEntity mainOwner = clientGenerator.getClient();
        ClientEntity coowner = clientGenerator.getClient();

        Set<ApartmentEntity> apartments = new HashSet<>();
        ApartmentEntity apartment1 = apartmentGenerator.getApartmentWithFloor0();
        apartments.add(apartment1);
        ApartmentEntity apartment2 = apartmentGenerator.getApartmentWithFloor1();
        apartments.add(apartment2);
        apartment1.setMainOwner(mainOwner);
        apartment1.setOwners(Collections.singleton(mainOwner));
        apartment2.setMainOwner(mainOwner);
        Set<ClientEntity> owners = new HashSet<>();
        owners.add(mainOwner);
        owners.add(coowner);
        apartment2.setOwners(owners);
        mainOwner.setApartments(apartments);

        mainOwner = clientRepository.save(mainOwner);
        coowner = clientRepository.save(coowner);

        apartment1 = apartmentRepository.save(apartment1);
        apartment2 = apartmentRepository.save(apartment2);

        //when
        List<ClientEntity> result = clientRepository.findClientsWithMoreThanOneApartment();

        //then
        assertThat(result).isNotNull().isNotEmpty();
        assertEquals(3, result.size());
        List<Long> ids = result.stream().map(x -> x.getId()).collect(Collectors.toList());
        assertTrue(ids.contains(mainOwner.getId()));
        assertTrue(ids.contains(coowner.getId()));
        assertTrue(ids.contains(client1.getId()));
    }

    @Test
    @Transactional
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
        entityManager.flush();
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