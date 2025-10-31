package kdg.be.prog6.kdg.common.events;

import org.jmolecules.event.types.DomainEvent;
import org.springframework.modulith.events.Externalized;

import java.time.LocalDateTime;
import java.util.UUID;

@Externalized("kdg.events::#{'restaurant.' + #this.restaurantId + '.order.accepted.v1'}")
public record OrderAcceptedEvent(
       UUID eventId,
       UUID orderId,
       String occurredAt,
       UUID restaurantId,
       PickupAddressDto pickupAddress,
       CoordinatesDto pickUpCoordinates,
       AddressDto dropoffAddress,
       CoordinatesDto dropoffCoordinates
) implements DomainEvent {}
