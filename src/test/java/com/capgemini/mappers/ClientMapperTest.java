package com.capgemini.mappers;

import com.capgemini.dao.ApartmentRepository;
import com.capgemini.dao.ClientRepository;
import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ApartmentEntity;
import com.capgemini.domain.ClientEntity;
import com.capgemini.types.AddressTO;
import com.capgemini.types.ClientTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class ClientMapperTest {
    @Autowired
    ClientMapper clientMapper;
    @Autowired
    ApartmentRepository apartmentRepository;
    @Autowired
    ClientRepository clientRepository;

    private AddressTO addressTO;
    private AddressInTable addressInTable;
    private Set<ApartmentEntity> apartmentEntities;
    private Set<Long> apartmentsIds;

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
        ApartmentEntity apartmentEntity1 = ApartmentEntity.builder()
                .balconyQty(1)
                .floor(0)
                .status("free")
                .price(new BigDecimal("50000.00"))
                .roomQty(3)
                .area(new BigDecimal(100.00))
                .address(addressInTable)
                .build();
        ApartmentEntity apartmentEntity2 = ApartmentEntity.builder()
                .balconyQty(1)
                .floor(0)
                .status("free")
                .price(new BigDecimal("50000.00"))
                .roomQty(3)
                .area(new BigDecimal(100.00))
                .address(addressInTable)
                .build();
        apartmentEntity1 = apartmentRepository.save(apartmentEntity1);
        apartmentEntity2 = apartmentRepository.save(apartmentEntity2);
        apartmentEntities = new HashSet<>();
        apartmentEntities.add(apartmentEntity1);
        apartmentEntities.add(apartmentEntity2);
        apartmentsIds = apartmentEntities.stream().map(x -> x.getId()).collect(Collectors.toSet());
    }

    @Test
    public void shouldNotMapToTO() {
        //when
        ClientTO mappedClient = clientMapper.toTO(null);

        //then
        assertThat(mappedClient).isNull();
    }

    @Test
    public void shouldMapToTO() {
        //given
        ClientEntity client = ClientEntity.builder()
                .telephone("telephone")
                .lastName("lastname")
                .firstName("firstname")
                .address(addressInTable)
                .apartments(apartmentEntities)
                .build();
        client = clientRepository.save(client);

        //when
        ClientTO mappedClient = clientMapper.toTO(client);

        //then
        assertThat(mappedClient).isNotNull();
        assertThat(mappedClient.getId()).isEqualTo(client.getId());
        assertThat(mappedClient.getVersion()).isEqualTo(client.getVersion());
        assertThat(mappedClient.getFirstName()).isEqualTo(client.getFirstName());
        assertThat(mappedClient.getLastName()).isEqualTo(client.getLastName());
        assertThat(mappedClient.getTelephone()).isEqualTo(client.getTelephone());
        assertThat(mappedClient.getAddress()).isNotNull();
        Set<Long> mappedApartmentsIds = mappedClient.getApartments();
        for(Long id : apartmentsIds) {
            assertThat(mappedApartmentsIds.contains(id)).isTrue();
        }
    }

    @Test
    public void shouldNotMapToEntity() {
        //when
        ClientEntity mappedClient = clientMapper.toEntity(null);

        //then
        assertThat(mappedClient).isNull();
    }

    @Test
    public void shouldMapToEntity() {
        //given
        ClientEntity client = ClientEntity.builder()
                .telephone("telephone")
                .lastName("lastname")
                .firstName("firstname")
                .address(addressInTable)
                .apartments(apartmentEntities)
                .build();
        client = clientRepository.save(client);

        ClientTO clientToMap = ClientTO.builder()
                .id(client.getId())
                .telephone("telephone")
                .lastName("lastname")
                .firstName("firstname")
                .address(addressTO)
                .apartments(apartmentsIds)
                .build();

        //when
        ClientEntity mappedClient = clientMapper.toEntity(clientToMap);

        //then
        assertThat(mappedClient).isNotNull();
        assertThat(mappedClient.getId()).isEqualTo(clientToMap.getId());
        assertThat(mappedClient.getVersion()).isEqualTo(clientToMap.getVersion());
        assertThat(mappedClient.getFirstName()).isEqualTo(clientToMap.getFirstName());
        assertThat(mappedClient.getLastName()).isEqualTo(clientToMap.getLastName());
        assertThat(mappedClient.getTelephone()).isEqualTo(clientToMap.getTelephone());
        assertThat(mappedClient.getAddress()).isNotNull();
        Set<Long> mappedApartmentsIds = mappedClient.getApartments().stream().map(x -> x.getId()).collect(Collectors.toSet());
        for(Long id : apartmentsIds) {
            assertThat(mappedApartmentsIds.contains(id)).isTrue();
        }
    }

    @Test
    public void shouldNotMap2TOs() {
        //when
        Set<ClientTO> mappedClient = clientMapper.map2TOs(new HashSet<>());

        //then
        assertThat(mappedClient).isNotNull().isEmpty();
    }

    @Test
    public void shouldMap2TOs() {
        //given
        ClientEntity client1 = ClientEntity.builder()
                .telephone("telephone")
                .lastName("lastname")
                .firstName("firstname")
                .address(addressInTable)
                .apartments(apartmentEntities)
                .build();
        client1 = clientRepository.save(client1);

        ClientEntity client2 = ClientEntity.builder()
                .telephone("telephone")
                .lastName("lastname")
                .firstName("firstname")
                .address(addressInTable)
                .apartments(apartmentEntities)
                .build();
        client2 = clientRepository.save(client2);
        Set<ClientEntity> clientsToMap = new HashSet<>();
        clientsToMap.add(client1);
        clientsToMap.add(client2);
        Set<Long> clientIds = clientsToMap.stream().map(x -> x.getId()).collect(Collectors.toSet());

        //when
        Set<ClientTO> mappedClient = clientMapper.map2TOs(clientsToMap);

        //then
        assertThat(mappedClient).isNotNull().isNotEmpty();
        Set<Long> mappedClientsIds = mappedClient.stream().map(x -> x.getId()).collect(Collectors.toSet());
        for(Long id : clientIds) {
            assertThat(mappedClientsIds.contains(id)).isTrue();
        }
    }

    @Test
    public void shouldNotMap2Entities() {
        //when
        Set<ClientEntity> mappedClient = clientMapper.map2Entities(new HashSet<>());

        //then
        assertThat(mappedClient).isNotNull().isEmpty();
    }

    @Test
    public void shouldMap2Entities() {
        //given
        ClientEntity client1 = ClientEntity.builder()
                .telephone("telephone")
                .lastName("lastname")
                .firstName("firstname")
                .address(addressInTable)
                .apartments(apartmentEntities)
                .build();
        client1 = clientRepository.save(client1);

        ClientTO clientToMap1 = ClientTO.builder()
                .id(client1.getId())
                .telephone("telephone")
                .lastName("lastname")
                .firstName("firstname")
                .address(addressTO)
                .apartments(apartmentsIds)
                .build();

        ClientEntity client2 = ClientEntity.builder()
                .telephone("telephone")
                .lastName("lastname")
                .firstName("firstname")
                .address(addressInTable)
                .apartments(apartmentEntities)
                .build();
        client2 = clientRepository.save(client2);

        ClientTO clientToMap2 = ClientTO.builder()
                .id(client2.getId())
                .telephone("telephone")
                .lastName("lastname")
                .firstName("firstname")
                .address(addressTO)
                .apartments(apartmentsIds)
                .build();

        Set<ClientTO> clientsToMap = new HashSet<>();
        clientsToMap.add(clientToMap1);
        clientsToMap.add(clientToMap2);
        Set<Long> clientIds = clientsToMap.stream().map(x -> x.getId()).collect(Collectors.toSet());

        //when
        Set<ClientEntity> mappedClients = clientMapper.map2Entities(clientsToMap);

        //then
        assertThat(mappedClients).isNotNull().isNotEmpty();
        Set<Long> mappedClientsIds = mappedClients.stream().map(x -> x.getId()).collect(Collectors.toSet());
        for(Long id : clientIds) {
            assertThat(mappedClientsIds.contains(id)).isTrue();
        }
    }

    @Test
    public void shouldNotMap2TOs2() {
        //when
        List<ClientTO> mappedClient = clientMapper.map2TOs(new ArrayList<>());

        //then
        assertThat(mappedClient).isNotNull().isEmpty();
    }

    @Test
    public void shouldMap2TOs2() {
        //given
        ClientEntity client1 = ClientEntity.builder()
                .telephone("telephone")
                .lastName("lastname")
                .firstName("firstname")
                .address(addressInTable)
                .apartments(apartmentEntities)
                .build();
        client1 = clientRepository.save(client1);

        ClientEntity client2 = ClientEntity.builder()
                .telephone("telephone")
                .lastName("lastname")
                .firstName("firstname")
                .address(addressInTable)
                .apartments(apartmentEntities)
                .build();
        client2 = clientRepository.save(client2);
        List<ClientEntity> clientsToMap = new ArrayList<>();
        clientsToMap.add(client1);
        clientsToMap.add(client2);
        Set<Long> clientIds = clientsToMap.stream().map(x -> x.getId()).collect(Collectors.toSet());

        //when
        List<ClientTO> mappedClient = clientMapper.map2TOs(clientsToMap);

        //then
        assertThat(mappedClient).isNotNull().isNotEmpty();
        Set<Long> mappedClientsIds = mappedClient.stream().map(x -> x.getId()).collect(Collectors.toSet());
        for(Long id : clientIds) {
            assertThat(mappedClientsIds.contains(id)).isTrue();
        }
    }

    @Test
    public void shouldNotMap2Entities2() {
        //when
        List<ClientEntity> mappedClient = clientMapper.map2Entities(new ArrayList<>());

        //then
        assertThat(mappedClient).isNotNull().isEmpty();
    }

    @Test
    public void shouldMap2Entities2() {
        //given
        ClientEntity client1 = ClientEntity.builder()
                .telephone("telephone")
                .lastName("lastname")
                .firstName("firstname")
                .address(addressInTable)
                .apartments(apartmentEntities)
                .build();
        client1 = clientRepository.save(client1);

        ClientTO clientToMap1 = ClientTO.builder()
                .id(client1.getId())
                .telephone("telephone")
                .lastName("lastname")
                .firstName("firstname")
                .address(addressTO)
                .apartments(apartmentsIds)
                .build();

        ClientEntity client2 = ClientEntity.builder()
                .telephone("telephone")
                .lastName("lastname")
                .firstName("firstname")
                .address(addressInTable)
                .apartments(apartmentEntities)
                .build();
        client2 = clientRepository.save(client2);

        ClientTO clientToMap2 = ClientTO.builder()
                .id(client2.getId())
                .telephone("telephone")
                .lastName("lastname")
                .firstName("firstname")
                .address(addressTO)
                .apartments(apartmentsIds)
                .build();

        List<ClientTO> clientsToMap = new ArrayList<>();
        clientsToMap.add(clientToMap1);
        clientsToMap.add(clientToMap2);
        Set<Long> clientIds = clientsToMap.stream().map(x -> x.getId()).collect(Collectors.toSet());

        //when
        List<ClientEntity> mappedClients = clientMapper.map2Entities(clientsToMap);

        //then
        assertThat(mappedClients).isNotNull().isNotEmpty();
        Set<Long> mappedClientsIds = mappedClients.stream().map(x -> x.getId()).collect(Collectors.toSet());
        for(Long id : clientIds) {
            assertThat(mappedClientsIds.contains(id)).isTrue();
        }
    }
}