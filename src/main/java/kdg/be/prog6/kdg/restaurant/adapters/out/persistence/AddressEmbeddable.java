package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;

import jakarta.persistence.Embeddable;
import kdg.be.prog6.kdg.restaurant.domain.Address;

@Embeddable
public class AddressEmbeddable {
    private String street;
    private String city;
    private String zipCode;

    public AddressEmbeddable() {
    }

    public AddressEmbeddable(String street, String city, String zipCode) {
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }

    public Address toDomain() {
        return new Address(street, city, zipCode);
    }

    public static AddressEmbeddable from(Address address) {
        return new AddressEmbeddable(
                address.street(),
                address.city(),
                address.zipCode()
        );
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }
}
