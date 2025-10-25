package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;

import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;

public interface RestaurantRepositoryPort {
    void save(Restaurant restaurant);
    Restaurant findById(RestaurantId id);
}
