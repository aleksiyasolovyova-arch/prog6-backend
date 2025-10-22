package kdg.be.prog6.kdg.restaurant.domain;

import java.util.Set;

public record DishDetails(String name,
                          DishType type,
                          Set<FoodTag> foodTags,
                          String description,
                          Money price,
                          String pictureUrl) {
}
