package kdg.be.prog6.kdg.restaurant.core;

import kdg.be.prog6.kdg.restaurant.adapters.in.response.DishDetailsResponse;
import kdg.be.prog6.kdg.restaurant.adapters.out.persistence.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.Dish;
import kdg.be.prog6.kdg.restaurant.domain.DishId;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;
import kdg.be.prog6.kdg.common.DishNotFoundException;
import kdg.be.prog6.kdg.common.RestaurantNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ViewDishDetailsUseCaseImpl {
    private final RestaurantRepositoryPort restaurantRepository;

    public ViewDishDetailsUseCaseImpl(RestaurantRepositoryPort restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional(readOnly = true)
    public DishDetailsResponse getDishDetails(RestaurantId restaurantId, DishId dishId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        Dish dish = restaurant.getPublishedDishes().stream()
                .filter(d -> d.getId().equals(dishId))
                .findFirst()
                .orElseThrow(() -> new DishNotFoundException("Dish not found in this restaurant"));

        return new DishDetailsResponse(
                dish.getId().uuid(),
                restaurant.getId().uuid(),
                dish.getName(),
                dish.getType().name(),
                dish.getFoodTags(),
                dish.getDescription(),
                dish.getPrice().amount(),
                dish.getPrice().currency(),
                dish.getPictureUrl(),
                dish.isAvailableForOrder(),
                restaurant.getDefaultPreparationTime().toMinutes()
        );
    }
}
