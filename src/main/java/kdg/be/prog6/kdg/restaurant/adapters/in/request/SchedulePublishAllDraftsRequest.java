package kdg.be.prog6.kdg.restaurant.adapters.in.request;

import java.time.LocalDateTime;

public record SchedulePublishAllDraftsRequest(
        LocalDateTime publishAt
) {
}
