package kdg.be.prog6.kdg.restaurant.ports.in;

import java.time.LocalDateTime;
import java.util.UUID;

public record SchedulePublishAllDraftsCommand(
        UUID restaurantId,
        LocalDateTime publishAt
) {
}
