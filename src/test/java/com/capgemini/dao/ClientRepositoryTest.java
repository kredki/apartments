package com.capgemini.dao;

import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ClientEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ClientRepositoryTest {
    @Autowired
    ClientRepository clientRepository;

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
        clientRepository.save(client1);
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