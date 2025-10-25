package kdg.be.prog6.kdg.common.events;

import java.util.UUID;

public record ScheduledPublishFailedEvent(
        UUID restaurantId,
        String warning,
        int sizeOfScheduledDishes
) implements RestaurantDomainEvent{
}
