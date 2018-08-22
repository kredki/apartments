package com.capgemini.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity for client.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CLIENTS")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ClientEntity extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Version
    public Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "FIRST_NAME", nullable = false, length = 50)
    private String firstName;
    @Column(name = "LAST_NAME", nullable = false, length = 50)
    private String lastName;
    @Embedded
    private AddressInTable address;
    @Column(name = "TELEPHONE", nullable = false, length = 50)
    private String telephone;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "owners")
    Set<ApartmentEntity> apartments = new HashSet<>();
}
