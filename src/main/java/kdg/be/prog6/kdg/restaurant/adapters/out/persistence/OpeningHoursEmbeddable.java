package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import kdg.be.prog6.kdg.restaurant.domain.OpeningHours;
import kdg.be.prog6.kdg.restaurant.domain.TimeRange;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

public class OpeningHoursEmbeddable {
    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;
    private String sunday;

    protected OpeningHoursEmbeddable() {}

    private OpeningHoursEmbeddable(
            String monday, String tuesday, String wednesday,
            String thursday, String friday, String saturday, String sunday
    ) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    public static OpeningHoursEmbeddable from(OpeningHours openingHours) {
        // Convert domain OpeningHours back to string format per day
        return new OpeningHoursEmbeddable(
                formatDay(openingHours, DayOfWeek.MONDAY),
                formatDay(openingHours, DayOfWeek.TUESDAY),
                formatDay(openingHours, DayOfWeek.WEDNESDAY),
                formatDay(openingHours, DayOfWeek.THURSDAY),
                formatDay(openingHours, DayOfWeek.FRIDAY),
                formatDay(openingHours, DayOfWeek.SATURDAY),
                formatDay(openingHours, DayOfWeek.SUNDAY)
        );
    }

    private static String formatDay(OpeningHours hours, DayOfWeek day) {
        List<TimeRange> ranges = hours.getOpeningTimesFor(day);
        if (ranges.isEmpty()) return "";

        return ranges.stream()
                .map(r -> r.start() + "-" + r.end())
                .collect(Collectors.joining(","));
    }

    public OpeningHours toDomain() {
        return OpeningHours.of(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
    }

    // Getters for JPA
    public String getMonday() { return monday; }
    public String getTuesday() { return tuesday; }
    public String getWednesday() { return wednesday; }
    public String getThursday() { return thursday; }
    public String getFriday() { return friday; }
    public String getSaturday() { return saturday; }
    public String getSunday() { return sunday; }
}