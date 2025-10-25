package kdg.be.prog6.kdg.restaurant.domain;

import java.sql.Time;


public record PreparationTime(int minutes) {
    public static PreparationTime ofMinutes(int minutes) {
        if (minutes <= 0) {
            throw new IllegalArgumentException("Preparation time must be positive");
        }
        if (minutes > 1440) { // More than 24 hours
            throw new IllegalArgumentException("Preparation time cannot exceed 24 hours");
        }
        return new PreparationTime(minutes);
    }

    public int toMinutes() {
        return minutes;
    }
}
