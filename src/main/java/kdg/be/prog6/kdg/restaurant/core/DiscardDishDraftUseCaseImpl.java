package kdg.be.prog6.kdg.restaurant.core;

import jakarta.transaction.Transactional;
import kdg.be.prog6.kdg.restaurant.adapters.out.persistence.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.common.RestaurantNotFoundException;
import kdg.be.prog6.kdg.restaurant.ports.in.DiscardDishDraftCommand;
import kdg.be.prog6.kdg.restaurant.ports.in.DiscardDraftPort;
import org.springframework.stereotype.Service;

@Service
public class DiscardDishDraftUseCaseImpl implements DiscardDraftPort {
    final private RestaurantRepositoryPort restaurantRepository;

    public DiscardDishDraftUseCaseImpl(RestaurantRepositoryPort restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    @Transactional
    public void discardDraft(DiscardDishDraftCommand command) {
        Restaurant restaurant = restaurantRepository.findById(command.restaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));
        restaurant.discardDraft(command.draftId());
        restaurantRepository.save(restaurant);
    }
}
