package kdg.be.prog6.kdg.restaurant.core;

import jakarta.transaction.Transactional;
import kdg.be.prog6.kdg.restaurant.adapters.out.persistence.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.DishId;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.common.RestaurantNotFoundException;
import kdg.be.prog6.kdg.restaurant.ports.in.PublishDishDraftCommand;
import kdg.be.prog6.kdg.restaurant.ports.in.PublishDishPort;
import org.springframework.stereotype.Service;

@Service
public class PublishDishUseCaseImpl implements PublishDishPort {
    private final RestaurantRepositoryPort restaurantRepository;

    public PublishDishUseCaseImpl(RestaurantRepositoryPort restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    @Transactional
    public DishId publishDraft(PublishDishDraftCommand cmd) {
        Restaurant restaurant = restaurantRepository.findById(cmd.restaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with this Id not found"));

        DishId publishedDishId = restaurant.publishDraft(cmd.draftId());  // ← Get returned ID

        restaurantRepository.save(restaurant);

        return publishedDishId;
    }

}
