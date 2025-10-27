package kdg.be.prog6.kdg.restaurant.core;

import jakarta.transaction.Transactional;
import kdg.be.prog6.kdg.restaurant.adapters.out.persistence.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.DishDraft;
import kdg.be.prog6.kdg.restaurant.domain.DraftId;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.common.RestaurantNotFoundException;
import kdg.be.prog6.kdg.restaurant.ports.in.CreateDishDraftCommand;
import kdg.be.prog6.kdg.restaurant.ports.in.CreateDishDraftPort;
import org.springframework.stereotype.Service;


@Service
public class CreateDishDraftUseCaseImpl implements CreateDishDraftPort {
    private final RestaurantRepositoryPort restaurantRepository;

    public CreateDishDraftUseCaseImpl(RestaurantRepositoryPort restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    @Transactional
    public DraftId createDishDraft(CreateDishDraftCommand cmd) {
        Restaurant restaurant = restaurantRepository.findById(cmd.restaurantId()).orElseThrow(() -> new RestaurantNotFoundException(
                "Restaurant not found with ID: " + cmd.restaurantId()));
        DishDraft draft = restaurant.createDraftForNewDish(cmd.details());
        restaurantRepository.save(restaurant);
        return draft.getId();
    }
}
