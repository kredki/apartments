package com.capgemini.types;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * TO for client
 */
@Getter
@Setter
@Builder
public class ClientTO {
    private Long version;
    private Long id;
    private String firstName;
    private String lastName;
    private AddressTO address;
    private String telephone;
    Set<Long> apartments;

    public Set<Long> getApartments() {
        return Objects.isNull(apartments) ? new HashSet<>() : apartments;
    }
}
