package kdg.be.prog6.kdg.common.events;

import java.util.UUID;

public record DishMarkedInStockEvent(
        UUID dishId,
        UUID restaurantId
) implements RestaurantDomainEvent {
}
