package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;

import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;

import java.util.Optional;

public interface RestaurantRepositoryPort {
    void save(Restaurant restaurant);
    Optional<Restaurant> findById(RestaurantId id);
}
