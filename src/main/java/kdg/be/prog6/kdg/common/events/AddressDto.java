package kdg.be.prog6.kdg.common.events;

public record AddressDto(
        String street,
        String number,
        String postalCode,
        String city
) {}
