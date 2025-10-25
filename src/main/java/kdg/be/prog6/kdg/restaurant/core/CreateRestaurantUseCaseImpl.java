package kdg.be.prog6.kdg.restaurant.core;

import jakarta.transaction.Transactional;
import kdg.be.prog6.kdg.restaurant.adapters.out.persistence.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;
import kdg.be.prog6.kdg.restaurant.ports.in.CreateRestaurantCommand;
import kdg.be.prog6.kdg.restaurant.ports.in.CreateRestaurantPort;
import org.springframework.stereotype.Service;

@Service
public class CreateRestaurantUseCaseImpl implements CreateRestaurantPort {
    private final RestaurantRepositoryPort repo;

    public CreateRestaurantUseCaseImpl(RestaurantRepositoryPort repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public RestaurantId createRestaurant(CreateRestaurantCommand cmd) {
        Restaurant restaurant = Restaurant.create(
                cmd.ownerId(),
                cmd.name(),
                cmd.address(),
                cmd.email(),
                cmd.pictureUrls(),
                cmd.cuisineType(),
                cmd.defaultPreparationTime(),
                cmd.openingHours()
        );
        repo.save(restaurant);
        return restaurant.getId();
    }

}
