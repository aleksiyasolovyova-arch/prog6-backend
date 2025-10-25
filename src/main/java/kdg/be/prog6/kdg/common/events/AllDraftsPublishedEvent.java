package kdg.be.prog6.kdg.common.events;

import java.util.UUID;

public record AllDraftsPublishedEvent(
        UUID restaurantId,
        int draftCount
) implements RestaurantDomainEvent {
}
