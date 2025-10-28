package kdg.be.prog6.kdg.order.domain;

public record DeliveryAddress(
        String street,
        String number,
        String postalCode,
        String city,
        String country
) {
    public DeliveryAddress {
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Street is required");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City is required");
        }
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Country is required");
        }
    }
}
