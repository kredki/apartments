package com.capgemini.service.impl;

import com.capgemini.dao.ClientRepository;
import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ClientEntity;
import com.capgemini.types.AddressTO;
import com.capgemini.types.ClientTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
public class ClientServiceImplTest {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientServiceImpl clientService;

    private AddressInTable addressInTable;
    private AddressTO addressTO;
    private ClientEntity client1;

    @Before
    public void setup() {
        addressInTable = AddressInTable.builder()
                .city("city")
                .no("no")
                .postalCode("code")
                .street("street")
                .build();
        addressTO = AddressTO.builder()
                .city("city")
                .no("no")
                .postalCode("code")
                .street("street")
                .build();
        client1 = ClientEntity.builder()
                .telephone("tel1")
                .lastName("Iksinski")
                .firstName("Andrzej")
                .address(addressInTable)
                .build();
        clientRepository.save(client1);
    }

    @Test
    public void shouldAddClient() {
        //given
        long clientsQtyBefore = clientRepository.count();
        ClientTO client = ClientTO.builder()
                .telephone("tel")
                .lastName("Nowak")
                .firstName("Jan")
                .address(addressTO)
                .build();

        //when
        ClientTO savedClient = clientService.addNewClient(client);

        //then
        assertEquals(clientsQtyBefore + 1, clientRepository.count());
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
        ClientTO clientA = ClientTO.builder()
                .id(client1Id)
                .telephone("tel2")
                .lastName("Kowalski")
                .firstName("Stefan")
                .address(addressTO)
                .version(versionBefore)
                .build();
        ClientTO clientB = ClientTO.builder()
                .id(client1Id)
                .telephone("tel3")
                .lastName("Kowalski2")
                .firstName("Stefan2")
                .address(addressTO)
                .version(versionBefore)
                .build();

        //when
        clientService.updateClient(clientA);
        try {
            clientService.updateClient(clientB);
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

    @Test
    public void shouldRemoveClient() {
        //given
        long clientsQtyBefore = clientRepository.count();
        ClientEntity client = ClientEntity.builder()
                .telephone("tel")
                .lastName("Nowak")
                .firstName("Jan")
                .address(addressInTable)
                .build();
        client = clientRepository.save(client);
        Long clientId = client.getId();

        //when
        clientService.removeClient(clientId);

        //then
        assertEquals(clientsQtyBefore - 1, clientRepository.count());
        List<ClientEntity> clients = clientRepository.findAll();
        List<Long> ids = new ArrayList<>();
        for (ClientEntity clientAfter : clients) {
            ids.add(clientAfter.getId());
        }
        assertFalse(ids.contains(clientId));
    }

    @Test
    public void shouldFindAllClients() {
        //given
        long clientsQtyBefore = clientRepository.count();
        List<ClientEntity> clientsBefore = clientRepository.findAll();
        List<Long> ids = new ArrayList<>();
        for (ClientEntity client : clientsBefore) {
            ids.add(client.getId());
        }

        //when
        List<ClientTO> clientsAfter = clientService.findAll();

        //then
        assertEquals(clientsQtyBefore, clientsAfter.size());
        for (ClientTO clientAfter : clientsAfter) {
            assertTrue(ids.contains(clientAfter.getId()));
        }
    }

    @Test
    public void shouldFindById() {
        //given
        Long client1Id = client1.getId();
        long clientsQtyBefore = clientRepository.count();
        List<ClientEntity> clientsBefore = clientRepository.findAll();
        List<Long> ids = new ArrayList<>();
        for (ClientEntity client : clientsBefore) {
            ids.add(client.getId());
        }

        //when
        ClientTO foundClient = clientService.findById(client1Id);

        //then
        assertNotNull(foundClient);
        assertEquals(client1Id, foundClient.getId());
        assertEquals(client1.getFirstName(), foundClient.getFirstName());
        assertEquals(client1.getTelephone(), foundClient.getLastName());
        assertEquals(client1.getTelephone(), foundClient.getTelephone());
    }
}