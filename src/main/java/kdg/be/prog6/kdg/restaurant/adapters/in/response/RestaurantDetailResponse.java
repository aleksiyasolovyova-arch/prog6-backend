package kdg.be.prog6.kdg.restaurant.adapters.in.response;

import java.util.List;
import java.util.UUID;

public record RestaurantDetailResponse(
        UUID id,
        String name,
        String cuisineType,
        String email,
        String street,
        String city,
        String postalCode,
        List<String> pictureUrls,
        double defaultPreparationTimeMinutes,
        OpeningHoursResponse openingHours,
        boolean isOpen,
        int publishedDishesCount
) {
    public record OpeningHoursResponse(
            String monday,
            String tuesday,
            String wednesday,
            String thursday,
            String friday,
            String saturday,
            String sunday
    ) {}
}
