package kdg.be.prog6.kdg.restaurant.core;

import jakarta.transaction.Transactional;
import kdg.be.prog6.kdg.restaurant.adapters.out.persistence.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.DishDraft;
import kdg.be.prog6.kdg.restaurant.domain.DraftId;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.common.RestaurantNotFoundException;
import kdg.be.prog6.kdg.restaurant.ports.in.CreateDraftForEditingCommand;
import kdg.be.prog6.kdg.restaurant.ports.in.CreateDraftForEditingPort;
import org.springframework.stereotype.Service;

@Service
public class CreateDraftForEditingUseCaseImpl implements CreateDraftForEditingPort {
    private final RestaurantRepositoryPort restaurantRepository;

    public CreateDraftForEditingUseCaseImpl(RestaurantRepositoryPort restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    @Transactional
    public DraftId createDraftForEditing(CreateDraftForEditingCommand command) {
        Restaurant restaurant = restaurantRepository.findById(command.restaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));
        DishDraft draft = restaurant.createDraftForExistingDish(command.dishId());
        restaurantRepository.save(restaurant);
        return draft.getId();
    }
}
