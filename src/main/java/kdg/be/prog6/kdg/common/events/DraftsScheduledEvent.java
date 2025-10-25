package kdg.be.prog6.kdg.common.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record DraftsScheduledEvent(
        UUID restaurantId,
        LocalDateTime publishAt,
        int draftCount
) implements RestaurantDomainEvent {
}
