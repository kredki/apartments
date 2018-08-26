package com.capgemini.mappers;

import com.capgemini.dao.ApartmentRepository;
import com.capgemini.dao.BuildingRepository;
import com.capgemini.dao.ClientRepository;
import com.capgemini.domain.AddressInTable;
import com.capgemini.domain.ApartmentEntity;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.domain.ClientEntity;
import com.capgemini.testutils.ClientGenerator;
import com.capgemini.types.AddressTO;
import com.capgemini.types.ApartmentTO;
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
public class ApartmentMapperTest {
    @Autowired
    private ApartmentMapper apartmentMapper;
    @Autowired
    BuildingRepository buildingRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientGenerator clientGenerator;
    @Autowired
    ApartmentRepository apartmentRepository;

    private AddressTO addressTO;
    private AddressInTable addressInTable;
    private BuildingEntity building;
    private ClientEntity mainOwner;
    private Set<ClientEntity> owners = new HashSet<>();
    private Long mainOwnerId;
    private Set<Long> ownersIds = new HashSet<>();
    private Long buildingId;

    @Before
    public void setup() {
        addressTO = AddressTO.builder()
                .street("street")
                .postalCode("code")
                .no("no").city("city")
                .build();
        addressInTable = AddressInTable.builder()
                .street("street")
                .postalCode("code")
                .no("no").city("city")
                .build();
        building = BuildingEntity.builder()
                .address(addressInTable)
                .apartmentsQty(0)
                .description("dsc")
                .floorQty(5)
                .isElevatorPresent(false)
                .apartments(new HashSet<>())
                .build();
        building = buildingRepository.save(building);
        buildingId = building.getId();
        mainOwner = clientGenerator.getClient();
        ClientEntity coowner = clientGenerator.getClient();
        mainOwner = clientRepository.save(mainOwner);
        coowner = clientRepository.save(coowner);
        mainOwnerId = mainOwner.getId();
        ownersIds.add(mainOwner.getId());
        ownersIds.add(coowner.getId());
        owners.add(mainOwner);
        owners.add(coowner);
    }

    @Test
    public void shouldNotMapToTO() {
        //when
        ApartmentTO mappedApartment = apartmentMapper.toTO(null);

        //then
        assertThat(mappedApartment).isNull();
    }

    @Test
    public void shouldMapToTO() {
        //given
        ApartmentEntity apartmentToMap = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building)
                .floor(0)
                .roomQty(4)
                .mainOwner(mainOwner)
                .owners(owners)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();

        //when
        ApartmentTO mappedApartment = apartmentMapper.toTO(apartmentToMap);

        //then
        assertThat(mappedApartment).isNotNull();
        assertThat(mappedApartment.getId()).isEqualTo(apartmentToMap.getId());
        assertThat(mappedApartment.getStatus()).isEqualTo(apartmentToMap.getStatus());
        assertThat(mappedApartment.getRoomQty()).isEqualTo(apartmentToMap.getRoomQty());
        assertThat(mappedApartment.getPrice()).isEqualTo(apartmentToMap.getPrice());
        assertThat(mappedApartment.getFloor()).isEqualTo(apartmentToMap.getFloor());
        assertThat(mappedApartment.getBalconyQty()).isEqualTo(apartmentToMap.getBalconyQty());
        assertThat(mappedApartment.getArea()).isEqualTo(apartmentToMap.getArea());
        assertThat(mappedApartment.getMainOwner()).isEqualTo(apartmentToMap.getMainOwner().getId());
        Set<Long> resultOwnersIds = mappedApartment.getOwners();
        assertThat(resultOwnersIds).isNotNull().isNotEmpty();
        for (Long id : ownersIds) {
            assertThat(resultOwnersIds.contains(id)).isTrue();
        }
    }

    @Test
    public void shouldNotMapToEntity() {
        //when
        ApartmentEntity mappedApartment = apartmentMapper.toEntity(null);

        //then
        assertThat(mappedApartment).isNull();
    }

    @Test
    public void shouldMapToEntity() {
        //given
        ApartmentTO apartmentToMap = ApartmentTO.builder()
                .address(addressTO)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(buildingId)
                .floor(0)
                .roomQty(4)
                .mainOwner(mainOwnerId)
                .owners(ownersIds)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();

        //when
        ApartmentEntity mappedApartment = apartmentMapper.toEntity(apartmentToMap);

        //then
        assertThat(mappedApartment).isNotNull();
        assertThat(mappedApartment.getId()).isEqualTo(apartmentToMap.getId());
        assertThat(mappedApartment.getStatus()).isEqualTo(apartmentToMap.getStatus());
        assertThat(mappedApartment.getRoomQty()).isEqualTo(apartmentToMap.getRoomQty());
        assertThat(mappedApartment.getPrice()).isEqualTo(apartmentToMap.getPrice());
        assertThat(mappedApartment.getFloor()).isEqualTo(apartmentToMap.getFloor());
        assertThat(mappedApartment.getBalconyQty()).isEqualTo(apartmentToMap.getBalconyQty());
        assertThat(mappedApartment.getArea()).isEqualTo(apartmentToMap.getArea());
        assertThat(mappedApartment.getMainOwner().getId()).isEqualTo(apartmentToMap.getMainOwner());
        Set<Long> resultOwnersIds = mappedApartment.getOwners().stream().map(x -> x.getId()).collect(Collectors.toSet());
        assertThat(resultOwnersIds).isNotNull().isNotEmpty();
        for (Long id : ownersIds) {
            assertThat(resultOwnersIds.contains(id)).isTrue();
        }
    }

    @Test
    public void shouldNotMap2TOs() {
        //when
        Set<ApartmentTO> mappedApartment = apartmentMapper.map2TOs(new HashSet<>());

        //then
        assertThat(mappedApartment).isEmpty();
    }

    @Test
    public void shouldMap2TOs() {
        //given
        ApartmentEntity apartmentToMap1 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building)
                .floor(0)
                .roomQty(4)
                .mainOwner(mainOwner)
                .owners(owners)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        ApartmentEntity apartmentToMap2 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building)
                .floor(0)
                .roomQty(4)
                .mainOwner(mainOwner)
                .owners(owners)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartmentToMap1 = apartmentRepository.save(apartmentToMap1);
        apartmentToMap2 = apartmentRepository.save(apartmentToMap2);
        Set<Long> apartmentsToMapIds = new HashSet<>();
        apartmentsToMapIds.add(apartmentToMap1.getId());
        apartmentsToMapIds.add(apartmentToMap2.getId());
        Set<ApartmentEntity> apartmentsToMap = new HashSet<>();
        apartmentsToMap.add(apartmentToMap1);
        apartmentsToMap.add(apartmentToMap2);

        //when
        Set<ApartmentTO> mappedApartments = apartmentMapper.map2TOs(apartmentsToMap);

        //then
        assertThat(mappedApartments).isNotNull().isNotEmpty();
        Set<Long> mappedApartmentsIds = mappedApartments.stream().map(x -> x.getId()).collect(Collectors.toSet());
        for (Long id: apartmentsToMapIds) {
            assertThat(mappedApartmentsIds.contains(id)).isTrue();
        }
    }

    @Test
    public void shouldNotMap2Entities() {
        //when
        Set<ApartmentEntity> mappedApartment = apartmentMapper.map2Entities(new HashSet<>());

        //then
        assertThat(mappedApartment).isEmpty();
    }

    @Test
    public void shouldMap2Entities() {
        //given
        ApartmentTO apartmentToMap1 = ApartmentTO.builder()
                .address(addressTO)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(buildingId)
                .floor(0)
                .roomQty(4)
                .mainOwner(mainOwnerId)
                .owners(ownersIds)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        ApartmentTO apartmentToMap2 = ApartmentTO.builder()
                .address(addressTO)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(buildingId)
                .floor(0)
                .roomQty(4)
                .mainOwner(mainOwnerId)
                .owners(ownersIds)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        Set<Long> apartmentsToMapIds = new HashSet<>();
        apartmentsToMapIds.add(apartmentToMap1.getId());
        apartmentsToMapIds.add(apartmentToMap2.getId());
        Set<ApartmentTO> apartmentsToMap = new HashSet<>();
        apartmentsToMap.add(apartmentToMap1);
        apartmentsToMap.add(apartmentToMap2);

        //when
        Set<ApartmentEntity> mappedApartments = apartmentMapper.map2Entities(apartmentsToMap);

        //then
        assertThat(mappedApartments).isNotNull().isNotEmpty();
        Set<Long> mappedApartmentsIds = mappedApartments.stream().map(x -> x.getId()).collect(Collectors.toSet());
        for (Long id: apartmentsToMapIds) {
            assertThat(mappedApartmentsIds.contains(id)).isTrue();
        }
    }

    @Test
    public void shouldNotMap2TOs2() {
        //when
        List<ApartmentTO> mappedApartment = apartmentMapper.map2TOs(new ArrayList<>());

        //then
        assertThat(mappedApartment).isEmpty();
    }

    @Test
    public void shouldMap2TOs2() {
        //given
        ApartmentEntity apartmentToMap1 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building)
                .floor(0)
                .roomQty(4)
                .mainOwner(mainOwner)
                .owners(owners)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        ApartmentEntity apartmentToMap2 = ApartmentEntity.builder()
                .address(addressInTable)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(building)
                .floor(0)
                .roomQty(4)
                .mainOwner(mainOwner)
                .owners(owners)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        apartmentToMap1 = apartmentRepository.save(apartmentToMap1);
        apartmentToMap2 = apartmentRepository.save(apartmentToMap2);
        Set<Long> apartmentsToMapIds = new HashSet<>();
        apartmentsToMapIds.add(apartmentToMap1.getId());
        apartmentsToMapIds.add(apartmentToMap2.getId());
        List<ApartmentEntity> apartmentsToMap = new ArrayList<>();
        apartmentsToMap.add(apartmentToMap1);
        apartmentsToMap.add(apartmentToMap2);

        //when
        List<ApartmentTO> mappedApartments = apartmentMapper.map2TOs(apartmentsToMap);

        //then
        assertThat(mappedApartments).isNotNull().isNotEmpty();
        Set<Long> mappedApartmentsIds = mappedApartments.stream().map(x -> x.getId()).collect(Collectors.toSet());
        for (Long id: apartmentsToMapIds) {
            assertThat(mappedApartmentsIds.contains(id)).isTrue();
        }
    }

    @Test
    public void shouldNotMap2Entities2() {
        //when
        List<ApartmentEntity> mappedApartment = apartmentMapper.map2Entities(new ArrayList<>());

        //then
        assertThat(mappedApartment).isEmpty();
    }

    @Test
    public void shouldMap2Entities2() {
        //given
        ApartmentTO apartmentToMap1 = ApartmentTO.builder()
                .address(addressTO)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(buildingId)
                .floor(0)
                .roomQty(4)
                .mainOwner(mainOwnerId)
                .owners(ownersIds)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        ApartmentTO apartmentToMap2 = ApartmentTO.builder()
                .address(addressTO)
                .area(new BigDecimal("50"))
                .balconyQty(1)
                .building(buildingId)
                .floor(0)
                .roomQty(4)
                .mainOwner(mainOwnerId)
                .owners(ownersIds)
                .price(new BigDecimal("120000"))
                .status("free")
                .build();
        Set<Long> apartmentsToMapIds = new HashSet<>();
        apartmentsToMapIds.add(apartmentToMap1.getId());
        apartmentsToMapIds.add(apartmentToMap2.getId());
        List<ApartmentTO> apartmentsToMap = new ArrayList<>();
        apartmentsToMap.add(apartmentToMap1);
        apartmentsToMap.add(apartmentToMap2);

        //when
        List<ApartmentEntity> mappedApartments = apartmentMapper.map2Entities(apartmentsToMap);

        //then
        assertThat(mappedApartments).isNotNull().isNotEmpty();
        Set<Long> mappedApartmentsIds = mappedApartments.stream().map(x -> x.getId()).collect(Collectors.toSet());
        for (Long id: apartmentsToMapIds) {
            assertThat(mappedApartmentsIds.contains(id)).isTrue();
        }
    }
}