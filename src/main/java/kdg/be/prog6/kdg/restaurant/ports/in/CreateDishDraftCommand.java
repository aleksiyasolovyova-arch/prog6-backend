package kdg.be.prog6.kdg.restaurant.ports.in;

import kdg.be.prog6.kdg.restaurant.domain.DishDetails;
import kdg.be.prog6.kdg.restaurant.domain.DishType;
import kdg.be.prog6.kdg.restaurant.domain.Money;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;

public record CreateDishDraftCommand(RestaurantId restaurantId,
                                     DishDetails details) {
}
