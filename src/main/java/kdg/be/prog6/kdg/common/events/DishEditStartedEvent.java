package kdg.be.prog6.kdg.common.events;

import java.util.UUID;

public record DishEditStartedEvent(
        UUID draftId,
        UUID dishId,
        UUID restaurantId
) implements RestaurantDomainEvent {
}
