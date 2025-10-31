package kdg.be.prog6.kdg.common.events;

import java.util.UUID;

public record OrderLocationEvent(
        UUID eventId,
        String occurredAt,
        UUID restaurantId,
        UUID orderId,
        LocationDto location
) {
}
