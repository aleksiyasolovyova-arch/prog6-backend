package kdg.be.prog6.kdg.restaurant.core;

import kdg.be.prog6.kdg.restaurant.adapters.out.persistence.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.restaurant.domain.exceptions.RestaurantNotFoundException;
import kdg.be.prog6.kdg.restaurant.ports.in.MarkInStockCommand;
import kdg.be.prog6.kdg.restaurant.ports.in.MarkInStockPort;
import org.springframework.stereotype.Service;

@Service
public class MarkInStockUseCaseImpl implements MarkInStockPort {
    private final RestaurantRepositoryPort restaurantRepository;
    public MarkInStockUseCaseImpl(RestaurantRepositoryPort restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public void markInStock(MarkInStockCommand command) {
        Restaurant restaurant = restaurantRepository.findById(command.restaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant Not Found"));

        restaurant.markDishInStock(command.dishId());

        restaurantRepository.save(restaurant);
    }
}
