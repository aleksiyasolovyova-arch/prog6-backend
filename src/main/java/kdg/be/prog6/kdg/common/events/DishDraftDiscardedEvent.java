package kdg.be.prog6.kdg.common.events;

import java.util.UUID;

public record DishDraftDiscardedEvent(
        UUID draftId,
        UUID restaurantId
) implements RestaurantDomainEvent {
}
