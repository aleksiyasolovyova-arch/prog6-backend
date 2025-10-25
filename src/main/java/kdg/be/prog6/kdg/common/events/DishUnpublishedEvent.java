package kdg.be.prog6.kdg.common.events;

import java.util.UUID;

public record DishUnpublishedEvent(
        UUID dishID,
        UUID restaurantId,
        String dishName
) implements RestaurantDomainEvent {
}
