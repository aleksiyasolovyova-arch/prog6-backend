package kdg.be.prog6.kdg.restaurant.ports.in;

import kdg.be.prog6.kdg.restaurant.domain.DishDetails;
import kdg.be.prog6.kdg.restaurant.domain.DraftId;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;

import java.util.UUID;

public record EditDishDraftCommand(
        DraftId draftId,
        UUID restaurantId,
        DishDetails details
) {
}
