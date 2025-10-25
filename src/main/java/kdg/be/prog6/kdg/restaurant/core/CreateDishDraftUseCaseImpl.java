package kdg.be.prog6.kdg.restaurant.core;

import jakarta.transaction.Transactional;
import kdg.be.prog6.kdg.restaurant.adapters.out.persistence.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.DishDetails;
import kdg.be.prog6.kdg.restaurant.domain.DishDraft;
import kdg.be.prog6.kdg.restaurant.domain.DraftId;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.restaurant.domain.exceptions.RestaurantNotFoundException;
import kdg.be.prog6.kdg.restaurant.ports.in.CreateDishDraftCommand;
import kdg.be.prog6.kdg.restaurant.ports.in.CreateDishDraftPort;
import org.springframework.stereotype.Service;

//TODO: FIX THE CREATION OF DRAFTS WITH DISHDETAILS AND WORK OUT THE WHOLE FLOW FOR CREATING A DRAFT
@Service
public class CreateDishDraftUseCaseImpl implements CreateDishDraftPort {
    private final RestaurantRepositoryPort restaurantRepository;

    public CreateDishDraftUseCaseImpl(RestaurantRepositoryPort restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    @Transactional
    public DraftId createDishDraft(CreateDishDraftCommand cmd) {
        Restaurant restaurant = restaurantRepository.findById(cmd.restaurantId());
        DishDetails details = new DishDetails(
                cmd.name(),
                cmd.description(),
                cmd.price(),
                cmd.dishType(),
                cmd.preparationTimeMinutes()
        );
        DishDraft draft = restaurant.createDraftForNewDish(details);
        restaurantRepository.save(restaurant);
        return draft.getId();
    }
}
