package com.capgemini.types;

import com.capgemini.Exceptions.IncorrectObjectException;
import lombok.Getter;
import lombok.Setter;

/**
 * TO for client
 */
@Getter
@Setter
public class ClientTO {
    private Long id;
    private String firstName;
    private String lastName;
    private AddressTO address;
    private String telephone;

    public ClientTO(Builder builder) {
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
        private AddressTO address;
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

        public Builder withAddress(AddressTO address) {
            this.address = address;
            return this;
        }

        public Builder withTelephone(String telephone) {
            this.telephone = telephone;
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
