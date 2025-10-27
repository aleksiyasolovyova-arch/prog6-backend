package kdg.be.prog6.kdg.restaurant.core;

import kdg.be.prog6.kdg.restaurant.adapters.in.response.RestaurantListResponse;
import kdg.be.prog6.kdg.restaurant.adapters.out.persistence.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrowseRestaurantsUseCaseImpl {
    private final RestaurantRepositoryPort restaurantRepository;

    public BrowseRestaurantsUseCaseImpl(RestaurantRepositoryPort restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional(readOnly = true)
    public RestaurantListResponse browseAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        List<RestaurantListResponse.RestaurantSummary> summaries = restaurants.stream()
                .map(restaurant -> new RestaurantListResponse.RestaurantSummary(
                        restaurant.getId().uuid(),
                        restaurant.getName(),
                        restaurant.getCuisineType().name(),
                        restaurant.getAddress().city(),
                        restaurant.getAddress().street(),
                        restaurant.getDefaultPreparationTime().toMinutes(),
                        restaurant.isAcceptingOrders(),
                        restaurant.getAvailableDishes().size()
                ))
                .collect(Collectors.toList());

        return new RestaurantListResponse(summaries);
    }

}
