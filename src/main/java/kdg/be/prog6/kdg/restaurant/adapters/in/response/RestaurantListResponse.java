package kdg.be.prog6.kdg.restaurant.adapters.in.response;

import java.util.List;
import java.util.UUID;

public record RestaurantListResponse(
        List<RestaurantSummary> restaurantSummaries
) {
    public record RestaurantSummary(
            UUID id,
            String name,
            String cuisineType,
            String city,
            String address,
            double defaultPreparationTimeMinutes,
            boolean isOpen,
            int availableDishesCount
    ) {}
}
