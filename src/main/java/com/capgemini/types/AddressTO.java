package com.capgemini.types;

import com.capgemini.Exceptions.IncorrectObjectException;
import lombok.Getter;
import lombok.Setter;

/**
 * TO for address
 */
@Getter
@Setter
public class AddressTO {
    String street;
    String no;
    String city;
    String postalCode;

    public AddressTO(Builder builder) {
        this.street = builder.street;
        this.no = builder.no;
        this.city = builder.city;
        this.postalCode = builder.postalCode;
    }
    
    public static class Builder {
        String street;
        String no;
        String city;
        String postalCode;

        public Builder withStreet(String street) {
            this.street = street;
            return this;
        }

        public Builder withNo(String no) {
            this.no = no;
            return this;
        }

        public Builder withCity(String city) {
            this.city = city;
            return this;
        }

        public Builder withPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public AddressTO build() {
            checkBeforeBuild();
            return new AddressTO(this);
        }

        private void checkBeforeBuild() {
            if (street == null || no == null || city == null || postalCode == null) {
                throw new IncorrectObjectException("Incorrect address to be created");
            }
        }
    }
}
