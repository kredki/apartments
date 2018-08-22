package com.capgemini.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Address embedded in entity.
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AddressInTable implements Serializable {
    @Column(name = "STREET", nullable = false, length = 50)
    String street;
    @Column(name = "NO", nullable = false, length = 50)
    String no;
    @Column(name = "CITY", nullable = false, length = 50)
    String city;
    @Column(name = "POSTAL_CODE", nullable = false, length = 50)
    String postalCode;
}
