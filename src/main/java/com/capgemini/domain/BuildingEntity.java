package com.capgemini.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Building entity.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BUILDINGS")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BuildingEntity extends AbstractEntity implements Serializable {
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<ApartmentEntity> apartments = new HashSet<>();

    public Set<ApartmentEntity> getApartments() {
        return Objects.isNull(apartments) ? new HashSet<>() : apartments;
    }
}
