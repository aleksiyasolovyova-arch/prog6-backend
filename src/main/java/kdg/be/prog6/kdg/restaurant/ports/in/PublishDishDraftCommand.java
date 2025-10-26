package kdg.be.prog6.kdg.restaurant.ports.in;

import kdg.be.prog6.kdg.restaurant.domain.DraftId;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;

public record PublishDishDraftCommand(
        RestaurantId restaurantId,
        DraftId draftId
) {
}
