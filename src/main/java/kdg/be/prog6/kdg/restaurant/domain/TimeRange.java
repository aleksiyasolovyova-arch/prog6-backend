package kdg.be.prog6.kdg.restaurant.domain;

import java.time.LocalTime;

public record TimeRange(LocalTime start, LocalTime end) {
    public TimeRange {
        if (end == null || start == null)
            throw new IllegalArgumentException("Times must not be null");
        if (!end.isAfter(start))
            throw new IllegalArgumentException("End time must be after start time");
    }

    public boolean includes(LocalTime time) {
        return !time.isBefore(start) && time.isBefore(end);
    }
}
