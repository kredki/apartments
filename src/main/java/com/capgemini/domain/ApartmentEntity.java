package com.capgemini.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Apartment Entity.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "APARTMENTS")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ApartmentEntity extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Version
    public Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "AREA", nullable = false)
    BigDecimal area;
    @Column(name = "ROOM_QUANTITY", nullable = false)
    Integer roomQty;
    @Column(name = "BALCONY_QUANTITY", nullable = false)
    Integer balconyQty;
    @Column(name = "FLOOR_NO", nullable = false)
    Integer floor;
    @Embedded
    AddressInTable address;
    @Column(name = "STATUS", nullable = false, length = 50)
    String status;
    @Column(name = "PRICE", nullable = false)
    BigDecimal price;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "APARTMENTS2CLIENTS",
            joinColumns = {@JoinColumn(name = "APARTMENT_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "CLIENT_ID", nullable = false, updatable = false)}
    )
    Set<ClientEntity> owners = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "BUILDING_ID")
    BuildingEntity building;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "MAIN_OWNER_ID")
    private ClientEntity mainOwner;
}
