package kdg.be.prog6.kdg.restaurant.core;

import kdg.be.prog6.kdg.restaurant.adapters.in.response.MenuResponse;
import kdg.be.prog6.kdg.restaurant.adapters.out.persistence.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;
import kdg.be.prog6.kdg.common.RestaurantNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class ViewMenuUseCaseImpl {
    private final RestaurantRepositoryPort restaurantRepository;

    public ViewMenuUseCaseImpl(RestaurantRepositoryPort restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional(readOnly = true)
    public MenuResponse getMenu(RestaurantId restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        var dishes = restaurant.getAvailableDishes().stream()  // Only available!
                .map(dish -> new MenuResponse.DishItemResponse(
                        dish.getId().uuid(),
                        dish.getName(),
                        dish.getType().name(),
                        dish.getFoodTags(),
                        dish.getDescription(),
                        dish.getPrice().amount(),
                        dish.getPrice().currency(),
                        dish.getPictureUrl(),
                        dish.isAvailableForOrder()
                ))
                .collect(Collectors.toList());

        return new MenuResponse(
                restaurant.getId().uuid(),
                restaurant.getName(),
                dishes
        );
    }
}
