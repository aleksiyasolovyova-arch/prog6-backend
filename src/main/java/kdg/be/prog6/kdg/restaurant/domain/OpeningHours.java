package kdg.be.prog6.kdg.restaurant.domain;

import kdg.be.prog6.kdg.restaurant.domain.TimeRange;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class OpeningHours {
    private final Map<DayOfWeek, List<TimeRange>> schedule;

    public OpeningHours(Map<DayOfWeek, List<TimeRange>> schedule) {
        this.schedule = copySchedule(schedule);
    }

    public static OpeningHours of(
            String monday,
            String tuesday,
            String wednesday,
            String thursday,
            String friday,
            String saturday,
            String sunday
    ) {
        Map<DayOfWeek, List<TimeRange>> schedule = new EnumMap<>(DayOfWeek.class);

        schedule.put(DayOfWeek.MONDAY, parseTimeRanges(monday));
        schedule.put(DayOfWeek.TUESDAY, parseTimeRanges(tuesday));
        schedule.put(DayOfWeek.WEDNESDAY, parseTimeRanges(wednesday));
        schedule.put(DayOfWeek.THURSDAY, parseTimeRanges(thursday));
        schedule.put(DayOfWeek.FRIDAY, parseTimeRanges(friday));
        schedule.put(DayOfWeek.SATURDAY, parseTimeRanges(saturday));
        schedule.put(DayOfWeek.SUNDAY, parseTimeRanges(sunday));

        return new OpeningHours(schedule);
    }

    // Helper method to parse time ranges from strings like "09:00-18:00"
    private static List<TimeRange> parseTimeRanges(String timeString) {
        if (timeString == null || timeString.isBlank()) {
            return List.of();
        }

        // Handle multiple ranges separated by comma: "09:00-12:00,14:00-18:00"
        return List.of(timeString.split(",")).stream()
                .map(String::strip)
                .filter(s -> !s.isBlank())
                .map(OpeningHours::parseTimeRange)
                .collect(java.util.stream.Collectors.toList());
    }

    private static TimeRange parseTimeRange(String range) {
        String[] parts = range.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid time range format: " + range);
        }

        LocalTime start = LocalTime.parse(parts[0].strip());
        LocalTime end = LocalTime.parse(parts[1].strip());

        return new TimeRange(start, end);
    }

    public boolean isOpenNow() {
        LocalDateTime now = LocalDateTime.now();
        return isOpenAt(now.getDayOfWeek(), now.toLocalTime());
    }
    public List<TimeRange> getOpeningTimesFor(DayOfWeek day) {
        return schedule.getOrDefault(day, List.of());
    }

    // Check if restaurant is open at a specific day and time
    public boolean isOpenAt(DayOfWeek day, LocalTime time) {
        return getOpeningTimesFor(day).stream()
                .anyMatch(range -> range.includes(time));
    }

    // Format day opening hours as a string (for persistence)
    public String formatDay(String dayName) {
        DayOfWeek day = DayOfWeek.valueOf(dayName.toUpperCase());
        List<TimeRange> ranges = getOpeningTimesFor(day);

        if (ranges.isEmpty()) {
            return "";
        }

        return ranges.stream()
                .map(r -> r.start() + "-" + r.end())
                .collect(Collectors.joining(","));
    }

    private Map<DayOfWeek, List<TimeRange>> copySchedule(Map<DayOfWeek, List<TimeRange>> input) {
        Map<DayOfWeek, List<TimeRange>> copy = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            copy.put(day, List.copyOf(input.getOrDefault(day, List.of())));
        }
        return Collections.unmodifiableMap(copy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OpeningHours that)) return false;
        return schedule.equals(that.schedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schedule);
    }
}
