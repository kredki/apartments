package com.capgemini.mappers;

import com.capgemini.domain.AddressInTable;
import com.capgemini.types.AddressTO;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AddressMapperTest {
    @Test
    public void shouldMapToTO() {
        //given
        AddressInTable address = AddressInTable.builder()
                .city("city")
                .no("no")
                .postalCode("code")
                .street("street")
                .build();

        //when
        AddressTO mappedAddress = AddressMapper.toTO(address);

        //then
        assertThat(mappedAddress).isNotNull();
        assertThat(mappedAddress.getCity()).isEqualTo(address.getCity());
        assertThat(mappedAddress.getNo()).isEqualTo(address.getNo());
        assertThat(mappedAddress.getPostalCode()).isEqualTo(address.getPostalCode());
        assertThat(mappedAddress.getStreet()).isEqualTo(address.getStreet());
    }

    @Test
    public void shouldNotMapToTO() {
        //when
        AddressTO mappedAddress = AddressMapper.toTO(null);

        //then
        assertThat(mappedAddress).isNull();
    }

    @Test
    public void shouldMapToInTable() {
        //given
        AddressTO address = AddressTO.builder()
                .city("city")
                .no("no")
                .postalCode("code")
                .street("street")
                .build();

        //when
        AddressInTable mappedAddress = AddressMapper.toInTable(address);

        //then
        assertThat(mappedAddress).isNotNull();
        assertThat(mappedAddress.getCity()).isEqualTo(address.getCity());
        assertThat(mappedAddress.getNo()).isEqualTo(address.getNo());
        assertThat(mappedAddress.getPostalCode()).isEqualTo(address.getPostalCode());
        assertThat(mappedAddress.getStreet()).isEqualTo(address.getStreet());
    }

    @Test
    public void shouldNotMapToInTable() {
        //when
        AddressInTable mappedAddress = AddressMapper.toInTable(null);

        //then
        assertThat(mappedAddress).isNull();
    }
}