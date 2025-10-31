package kdg.be.prog6.kdg.common.events;

import org.jmolecules.event.types.DomainEvent;

import java.util.UUID;

public record RestaurantContactUpdatedEvent(
        UUID restaurantId,
        UUID ownerId
) implements DomainEvent {
}
