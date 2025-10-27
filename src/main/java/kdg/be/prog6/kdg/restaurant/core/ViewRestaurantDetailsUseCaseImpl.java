package kdg.be.prog6.kdg.restaurant.core;

import kdg.be.prog6.kdg.restaurant.adapters.in.response.RestaurantDetailResponse;
import kdg.be.prog6.kdg.restaurant.adapters.out.persistence.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.OpeningHours;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;
import kdg.be.prog6.kdg.restaurant.domain.exceptions.RestaurantNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ViewRestaurantDetailsUseCaseImpl {
    final private RestaurantRepositoryPort restaurantRepository;

    public ViewRestaurantDetailsUseCaseImpl(RestaurantRepositoryPort restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional(readOnly = true)
    public RestaurantDetailResponse getRestaurantDetails(RestaurantId restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        return new RestaurantDetailResponse(
                restaurant.getId().uuid(),
                restaurant.getName(),
                restaurant.getCuisineType().name(),
                restaurant.getContactEmail().value(),
                restaurant.getAddress().street(),
                restaurant.getAddress().zipCode(),
                restaurant.getAddress().city(),
                restaurant.getPictureUrls(),
                restaurant.getDefaultPreparationTime().toMinutes(),
                mapOpeningHours(restaurant),
                restaurant.isAcceptingOrders(),
                restaurant.getPublishedDishes().size()
        );
    }

    private RestaurantDetailResponse.OpeningHoursResponse mapOpeningHours(Restaurant restaurant) {
        OpeningHours hours = restaurant.getOpeningHours();

        return new RestaurantDetailResponse.OpeningHoursResponse(
                hours.formatDay("MONDAY"),
                hours.formatDay("TUESDAY"),
                hours.formatDay("WEDNESDAY"),
                hours.formatDay("THURSDAY"),
                hours.formatDay("FRIDAY"),
                hours.formatDay("SATURDAY"),
                hours.formatDay("SUNDAY")
        );
    }
}
