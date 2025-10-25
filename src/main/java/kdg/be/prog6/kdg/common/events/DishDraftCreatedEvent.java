package kdg.be.prog6.kdg.common.events;

import java.util.UUID;

public record DishDraftCreatedEvent(
        UUID draftId,
        UUID restaurantId,
        boolean isNewDish
) implements RestaurantDomainEvent {
}
