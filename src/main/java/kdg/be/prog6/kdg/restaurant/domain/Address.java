package kdg.be.prog6.kdg.restaurant.domain;

public record Address(String street, String city, String zipCode) {
    public static Address of(String street, String city, String zipCode) {
        if (street == null || street.isBlank()) throw new IllegalArgumentException("Street required");
        if (city == null || city.isBlank()) throw new IllegalArgumentException("City required");
        if (zipCode == null || zipCode.isBlank()) throw new IllegalArgumentException("Zip code required");
        return new Address(street, city, zipCode);
    }
}
