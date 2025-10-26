package kdg.be.prog6.kdg.restaurant.domain;

import java.util.Collections;
import java.util.Set;

public record DishDetails(String name,
                          DishType type,
                          Set<FoodTag> foodTags,
                          String description,
                          Money price,
                          String pictureUrl) {
    public DishDetails {
        // Validate in compact constructor
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Dish name required");
        }
        if (type == null) {
            throw new IllegalArgumentException("Dish type required");
        }
        if (price == null) {
            throw new IllegalArgumentException("Price required");
        }
        // Make foodTags immutable
        if (foodTags == null) {
            foodTags = Collections.emptySet();
        } else {
            foodTags = Collections.unmodifiableSet(foodTags);
        }
    }
}
