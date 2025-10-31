package kdg.be.prog6.kdg.common.events;

public record PickupAddressEvent(
        String street,      // ✅ Not null!
        String number,      // ✅ Not null!
        String postalCode,  // ✅ Not null!
        String city,        // ✅ Not null!
        String country
) {
}
