package kdg.be.prog6.kdg.restaurant.ports.in;

import kdg.be.prog6.kdg.restaurant.domain.OwnerId;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;

import java.util.Optional;

public interface GetRestaurantByOwnerPort {
     Optional<Restaurant> getRestaurantByOwner(OwnerId ownerId);
}
