package kdg.be.prog6.kdg.restaurant.core;

import kdg.be.prog6.kdg.restaurant.adapters.in.response.PendingChangesResponse;
import kdg.be.prog6.kdg.restaurant.ports.out.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.common.RestaurantNotFoundException;
import kdg.be.prog6.kdg.restaurant.ports.in.GetPendingChangesPort;
import kdg.be.prog6.kdg.restaurant.ports.in.GetPendingChangesQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetPendingChangesUseCaseImpl implements GetPendingChangesPort {
    private final RestaurantRepositoryPort restaurantRepository;

    public GetPendingChangesUseCaseImpl(RestaurantRepositoryPort restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PendingChangesResponse getPendingChanges(GetPendingChangesQuery query) {
        Restaurant restaurant = restaurantRepository.findById(query.restaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));
        int pendingChangesCount = restaurant.getPendingChangesCount();
        return new PendingChangesResponse(pendingChangesCount);
    }
}
