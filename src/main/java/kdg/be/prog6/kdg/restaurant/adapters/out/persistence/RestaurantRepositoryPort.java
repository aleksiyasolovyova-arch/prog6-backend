package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;

import kdg.be.prog6.kdg.restaurant.domain.Restaurant;

public interface RestaurantRepositoryPort {
    void save(Restaurant restaurant);
}
