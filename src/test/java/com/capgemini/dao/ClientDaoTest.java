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
public class ClientDaoTest {
    @Autowired
    ClientDao clientDao;

    @Before
    public void setup() {

    }

    @Test
    public void shouldAddClient() {
        //given
        long clientsQty = clientDao.count();
        AddressInTable address = new AddressInTable.Builder().withCity("city").withNo("no").withPostalCode("code")
                .withStreet("street").build();
        ClientEntity client = new ClientEntity.Builder().withTelephone("tel").withLastName("Nowak").withFirstName("Jan")
                .withAddress(address).build();

        //when
        ClientEntity savedClient = clientDao.save(client);

        //then
        assertEquals(clientsQty + 1, clientDao.count());
        assertEquals(client.getFirstName(), savedClient.getFirstName());
        assertEquals(client.getLastName(), savedClient.getLastName());
        assertEquals(client.getTelephone(), savedClient.getTelephone());
    }
}