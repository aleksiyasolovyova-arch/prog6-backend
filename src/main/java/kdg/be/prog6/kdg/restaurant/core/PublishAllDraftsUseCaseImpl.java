package kdg.be.prog6.kdg.restaurant.core;

import jakarta.transaction.Transactional;
import kdg.be.prog6.kdg.restaurant.ports.out.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.common.RestaurantNotFoundException;
import kdg.be.prog6.kdg.restaurant.ports.in.PublishAllDraftsCommand;
import kdg.be.prog6.kdg.restaurant.ports.in.PublishAllDraftsPort;
import org.springframework.stereotype.Service;

@Service
public class PublishAllDraftsUseCaseImpl implements PublishAllDraftsPort {
    private final RestaurantRepositoryPort restaurantRepository;

    public PublishAllDraftsUseCaseImpl(RestaurantRepositoryPort restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    @Transactional
    public int publishAllDrafts(PublishAllDraftsCommand command) {
        Restaurant restaurant = restaurantRepository.findById(command.restaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        int countBefore = restaurant.getPendingChangesCount();
        restaurant.publishAllDrafts();
        restaurantRepository.save(restaurant);

        return countBefore;
        }
}
