package com.capgemini.types;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * TO for address
 */
@Getter
@Setter
@Builder
public class AddressTO {
    private String street;
    private String no;
    private String city;
    private String postalCode;
}
