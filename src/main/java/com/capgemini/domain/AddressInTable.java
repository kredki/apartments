package com.capgemini.domain;

import com.capgemini.Exceptions.IncorrectObjectException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class AddressInTable implements Serializable {
    @Column(name = "STREET", nullable = false, length = 50)
    String street;
    @Column(name = "NO", nullable = false, length = 50)
    String no;
    @Column(name = "CITY", nullable = false, length = 50)
    String city;
    @Column(name = "POSTAL_CODE", nullable = false, length = 50)
    String postalCode;

    public AddressInTable(Builder builder) {
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

        public AddressInTable build() {
            checkBeforeBuild();
            return new AddressInTable(this);
        }

        private void checkBeforeBuild() {
            if (street == null || no == null || city == null || postalCode == null) {
                throw new IncorrectObjectException("Incorrect address to be created");
            }
        }
    }
}
