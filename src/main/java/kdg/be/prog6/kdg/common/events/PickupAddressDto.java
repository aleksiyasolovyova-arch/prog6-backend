package kdg.be.prog6.kdg.common.events;

public record PickupAddressDto(
        String street,
        String number,
        String postalCode,
        String city
) {
}
