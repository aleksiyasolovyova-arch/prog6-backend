package kdg.be.prog6.kdg.restaurant.core;

import jakarta.transaction.Transactional;
import kdg.be.prog6.kdg.restaurant.ports.out.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;
import kdg.be.prog6.kdg.common.RestaurantNotFoundException;
import kdg.be.prog6.kdg.restaurant.ports.in.EditDishDraftCommand;
import kdg.be.prog6.kdg.restaurant.ports.in.EditDishDraftPort;
import org.springframework.stereotype.Service;

@Service
public class EditDishDraftUseCaseImpl implements EditDishDraftPort {
    private final RestaurantRepositoryPort restaurantRepository;

    public EditDishDraftUseCaseImpl(RestaurantRepositoryPort restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    @Transactional
    public void editDraft(EditDishDraftCommand command) {
        Restaurant restaurant = restaurantRepository.findById(RestaurantId.from(command.restaurantId()))
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));
        restaurant.editDraft(command.draftId(), command.details());
        restaurantRepository.save(restaurant);
    }
}
