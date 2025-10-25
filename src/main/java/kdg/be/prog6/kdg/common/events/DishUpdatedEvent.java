package kdg.be.prog6.kdg.common.events;

import java.util.UUID;

public record DishUpdatedEvent(
        UUID dishId,
        UUID restaurantId,
        String dishName
) implements RestaurantDomainEvent {
}
