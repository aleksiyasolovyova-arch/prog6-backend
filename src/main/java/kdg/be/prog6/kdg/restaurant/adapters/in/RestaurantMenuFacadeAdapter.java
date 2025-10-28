package kdg.be.prog6.kdg.restaurant.adapters.in;

import kdg.be.prog6.kdg.common.RestaurantBCMenuView;
import kdg.be.prog6.kdg.common.RestaurantMenuFacade;
import kdg.be.prog6.kdg.restaurant.adapters.out.persistence.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;
import kdg.be.prog6.kdg.common.MenuItemView;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantMenuFacadeAdapter implements RestaurantMenuFacade {
    private final RestaurantRepositoryPort restaurantRepository;

    public RestaurantMenuFacadeAdapter(RestaurantRepositoryPort restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public RestaurantBCMenuView getMenu(UUID restaurantId) {
        var restaurant = restaurantRepository.findById(RestaurantId.from(restaurantId))
                .orElse(null);

        if (restaurant == null) {
            return null;
        }

        var dishViews = restaurant.getAvailableDishes().stream()
                .map(dish -> new MenuItemView(
                        dish.getId().uuid(),
                        dish.getName(),
                        dish.getPrice().amount(),
                        dish.isAvailableForOrder()
                ))
                .collect(Collectors.toList());

        return new RestaurantBCMenuView(
                restaurant.getId().uuid(),
                restaurant.getName(),
                dishViews
        );
    }

    @Override
    public MenuItemView getDish(UUID restaurantId, UUID dishId) {
        var restaurant = restaurantRepository.findById(RestaurantId.from(restaurantId))
                .orElse(null);

        if (restaurant == null) {
            return null;
        }

        return restaurant.getPublishedDishes().stream()
                .filter(dish -> dish.getId().uuid().equals(dishId))
                .map(dish -> new MenuItemView(
                        dish.getId().uuid(),
                        dish.getName(),
                        dish.getPrice().amount(),
                        dish.isAvailableForOrder()
                ))
                .findFirst()
                .orElse(null);
    }
}
