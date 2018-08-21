package com.capgemini.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Building entity.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "BUILDINGS")
public class BuildingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Version
    public Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "DESCRIPTION", nullable = false, length = 200)
    private String description;
    @Embedded
    private AddressInTable address;
    @Column(name = "FLOOR_QUANTITY", nullable = false, length = 200)
    private Integer floorQty;
    @Column(name = "IS_ELEVATOR_PRESENT", nullable = false)
    private Boolean isElevatorPresent;
    @Column(name = "APARTMENT_QUANTITY", nullable = false)
    private Integer apartmentsQty;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "building")
    Set<ApartmentEntity> apartments = new HashSet<>();
}
