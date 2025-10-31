package kdg.be.prog6.kdg.restaurant.adapters.in.response;

import kdg.be.prog6.kdg.restaurant.domain.Restaurant;

import java.util.List;
import java.util.UUID;

public record RestaurantDetailResponse(
        UUID restaurantId,
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
    public static RestaurantDetailResponse toResponse(Restaurant restaurant) {
        return new RestaurantDetailResponse(
                restaurant.getId().uuid(),
                restaurant.getName(),
                restaurant.getCuisineType().name(),
                restaurant.getContactEmail().toString(),
                restaurant.getAddress().street(),
                restaurant.getAddress().city(),
                restaurant.getAddress().zipCode(),
                restaurant.getPictureUrls().stream().map(Object::toString).toList(),
                restaurant.getDefaultPreparationTime().toMinutes(),
                new RestaurantDetailResponse.OpeningHoursResponse(
                        restaurant.getOpeningHours().formatDay("monday"),
                        restaurant.getOpeningHours().formatDay("tuesday"),
                        restaurant.getOpeningHours().formatDay("wednesday"),
                        restaurant.getOpeningHours().formatDay("thursday"),
                        restaurant.getOpeningHours().formatDay("friday"),
                        restaurant.getOpeningHours().formatDay("saturday"),
                        restaurant.getOpeningHours().formatDay("sunday")
                ),
                restaurant.isAcceptingOrders(),
                restaurant.getPublishedDishes().size()
        );
    }
}
