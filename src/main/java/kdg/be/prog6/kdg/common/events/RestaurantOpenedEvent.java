package kdg.be.prog6.kdg.common.events;

import java.util.UUID;

public record RestaurantOpenedEvent(
        UUID restaurantId,
        boolean isManualOverride
) implements RestaurantDomainEvent {
}
