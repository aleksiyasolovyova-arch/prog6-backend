package kdg.be.prog6.kdg.restaurant.ports.in;

import kdg.be.prog6.kdg.restaurant.domain.DishType;
import kdg.be.prog6.kdg.restaurant.domain.Money;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;

public record CreateDishDraftCommand(RestaurantId restaurantId,
                                     String name,
                                     String description,
                                     Money price,
                                     DishType dishType,
                                     Integer preparationTimeMinutes) {
}
