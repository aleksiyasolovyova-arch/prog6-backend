package kdg.be.prog6.kdg.common.events;

import java.util.UUID;

public record RestaurantClosedEvent(
        UUID restaurantId,
        boolean isManualOverride
) implements RestaurantDomainEvent {
}
