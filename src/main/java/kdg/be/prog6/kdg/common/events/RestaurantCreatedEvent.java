package kdg.be.prog6.kdg.common.events;

import kdg.be.prog6.kdg.restaurant.domain.CuisineType;
import kdg.be.prog6.kdg.restaurant.domain.OwnerId;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;

import java.util.UUID;

public record RestaurantCreatedEvent(UUID restaurantId,
                                     UUID ownerId,
                                     String restaurantName,
                                     String cuisineType) implements RestaurantDomainEvent {
}
