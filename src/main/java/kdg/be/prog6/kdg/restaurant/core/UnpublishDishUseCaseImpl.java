package kdg.be.prog6.kdg.restaurant.core;

import jakarta.transaction.Transactional;
import kdg.be.prog6.kdg.restaurant.adapters.out.persistence.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.common.RestaurantNotFoundException;
import kdg.be.prog6.kdg.restaurant.ports.in.UnpublishDishCommand;
import kdg.be.prog6.kdg.restaurant.ports.in.UnpublishDishPort;
import org.springframework.stereotype.Service;

@Service
public class UnpublishDishUseCaseImpl implements UnpublishDishPort {
    private final RestaurantRepositoryPort restaurantRepository;

    public UnpublishDishUseCaseImpl(RestaurantRepositoryPort restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    @Transactional
    public void unpublishDraft(UnpublishDishCommand command) {
        Restaurant restaurant = restaurantRepository.findById(command.restaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        restaurant.unpublishDish(command.dishId());

        restaurantRepository.save(restaurant);
    }
}
