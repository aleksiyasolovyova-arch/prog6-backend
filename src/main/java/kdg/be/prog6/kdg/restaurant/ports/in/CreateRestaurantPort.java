package kdg.be.prog6.kdg.restaurant.ports.in;

import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;

public interface CreateRestaurantPort {
    RestaurantId createRestaurant(CreateRestaurantCommand command);
}
