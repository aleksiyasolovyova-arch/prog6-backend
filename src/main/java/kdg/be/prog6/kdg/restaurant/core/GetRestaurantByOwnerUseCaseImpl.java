package kdg.be.prog6.kdg.restaurant.core;

import kdg.be.prog6.kdg.restaurant.domain.OwnerId;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.restaurant.ports.in.GetRestaurantByOwnerPort;
import kdg.be.prog6.kdg.restaurant.ports.out.RestaurantRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetRestaurantByOwnerUseCaseImpl implements GetRestaurantByOwnerPort {
    private final RestaurantRepositoryPort repository;

    public GetRestaurantByOwnerUseCaseImpl(RestaurantRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Restaurant> getRestaurantByOwner(OwnerId ownerId) {
        return repository.findByOwnerId(ownerId);  // ✅ Query by owner
    }
}