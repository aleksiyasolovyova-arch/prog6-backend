package kdg.be.prog6.kdg.restaurant.ports.out;

import kdg.be.prog6.kdg.restaurant.domain.OwnerId;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepositoryPort {
    void save(Restaurant restaurant);
    Optional<Restaurant> findById(RestaurantId id);
    Optional<Restaurant> findByOwnerId(OwnerId ownerId);
    List<Restaurant> findAll();
}
