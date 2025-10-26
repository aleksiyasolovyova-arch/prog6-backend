package kdg.be.prog6.kdg.restaurant.core;

import jakarta.transaction.Transactional;
import kdg.be.prog6.kdg.restaurant.adapters.out.persistence.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.restaurant.domain.exceptions.RestaurantNotFoundException;
import kdg.be.prog6.kdg.restaurant.ports.in.MarkOutOfStockCommand;
import kdg.be.prog6.kdg.restaurant.ports.in.MarkOutOfStockPort;
import org.springframework.stereotype.Service;

@Service
public class MarkOutOfStockUseCaseImpl implements MarkOutOfStockPort {
    private final RestaurantRepositoryPort restaurantRepository;

    public MarkOutOfStockUseCaseImpl(RestaurantRepositoryPort restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    @Transactional
    public void markOutOfStock(MarkOutOfStockCommand command) {
    Restaurant restaurant = restaurantRepository.findById(command.restaurantId())
            .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));
    restaurant.markDishOutOfStock(command.dishId());
    restaurantRepository.save(restaurant);
    }
}
