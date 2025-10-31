package kdg.be.prog6.kdg.restaurant.adapters.in.request;

import kdg.be.prog6.kdg.common.events.AddressDto;
import kdg.be.prog6.kdg.restaurant.domain.Email;

public record UpdateContactRequest(
        Email email,
        AddressDto address
) {
}
