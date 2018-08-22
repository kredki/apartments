package com.capgemini.dao;

import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ClientEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
public class ClientRepositoryTest {
    @Autowired
    ClientRepository clientRepository;

    @Before
    public void setup() {

    }

    @Test
    public void shouldAddClient() {
        //given
        long clientsQty = clientRepository.count();
        AddressInTable address = AddressInTable.builder().city("city").no("no").postalCode("code").street("street").build();
        ClientEntity client = ClientEntity.builder().telephone("tel").lastName("Nowak").firstName("Jan").address(address)
                .build();

        //when
        ClientEntity savedClient = clientRepository.save(client);

        //then
        assertEquals(clientsQty + 1, clientRepository.count());
        assertEquals(client.getFirstName(), savedClient.getFirstName());
        assertEquals(client.getLastName(), savedClient.getLastName());
        assertEquals(client.getTelephone(), savedClient.getTelephone());
    }
}