package com.capgemini.types;

import com.capgemini.exceptions.IncorrectObjectException;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * TO for client
 */
@Getter
@Setter
public class ClientTO {
    private Long version;
    private Long id;
    private String firstName;
    private String lastName;
    private AddressTO address;
    private String telephone;
    Set<Long> apartments = new HashSet<>();

    public ClientTO(Builder builder) {
        this.version = builder.version;
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.address = builder.address;
        this.telephone = builder.telephone;
        this.apartments = builder.apartments;
    }

    public static class Builder {
        private Long version;
        private Long id;
        private String firstName;
        private String lastName;
        private AddressTO address;
        private String telephone;
        Set<Long> apartments = new HashSet<>();

        public Builder withVarsion(Long version) {
            this.version = version;
            return this;
        }

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

        public Builder withAddress(AddressTO address) {
            this.address = address;
            return this;
        }

        public Builder withTelephone(String telephone) {
            this.telephone = telephone;
            return this;
        }

        public Builder withApartments(Set<Long> apartments) {
            this.apartments.addAll(apartments);
            return this;
        }

        public ClientTO build() {
            checkBeforeBuild();
            return new ClientTO(this);
        }

        private void checkBeforeBuild() {
            if (firstName == null || lastName == null || address == null || telephone == null) {
                throw new IncorrectObjectException("Incorrect rental to be created");
            }
        }
    }
}
