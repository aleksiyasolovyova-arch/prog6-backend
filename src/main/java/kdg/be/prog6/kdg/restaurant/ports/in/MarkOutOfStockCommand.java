package kdg.be.prog6.kdg.restaurant.ports.in;

import kdg.be.prog6.kdg.restaurant.domain.DishId;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;

public record MarkOutOfStockCommand(
        RestaurantId restaurantId,
        DishId dishId
) {
}
