package kdg.be.prog6.kdg.restaurant.ports.in;

import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;

public record PublishAllDraftsCommand(
        RestaurantId restaurantId
) {
}
