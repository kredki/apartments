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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
        long clientsQty = clientRepository.count();
        ClientTO client = ClientTO.builder()
                .telephone("tel")
                .lastName("Nowak")
                .firstName("Jan")
                .address(addressTO)
                .build();

        //when
        ClientTO savedClient = clientService.addNewClient(client);

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
}