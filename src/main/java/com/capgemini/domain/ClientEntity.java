package com.capgemini.domain;

import com.capgemini.Exceptions.IncorrectObjectException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity for client.
 */
@Getter
@Setter
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

    public ClientEntity(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.address = builder.address;
        this.telephone = builder.telephone;
    }

    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private AddressInTable address;
        private String telephone;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withAddress(AddressInTable address) {
            this.address = address;
            return this;
        }

        public Builder withTelephone(String telephone) {
            this.telephone = telephone;
            return this;
        }

        public ClientEntity build() {
            checkBeforeBuild();
            return new ClientEntity(this);
        }

        private void checkBeforeBuild() {
            if (firstName == null || lastName == null || address == null || telephone == null) {
                throw new IncorrectObjectException("Incorrect rental to be created");
            }
        }
    }
}
