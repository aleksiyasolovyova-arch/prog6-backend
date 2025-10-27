package kdg.be.prog6.kdg.restaurant.adapters.in.response;

import kdg.be.prog6.kdg.restaurant.domain.FoodTag;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record DishDetailsResponse(
        UUID id,
        UUID restaurantId,
        String name,
        String type,
        Set<FoodTag> foodTags,
        String description,
        BigDecimal price,
        java.util.Currency currency,
        String pictureUrl,
        boolean availableForOrder,
        double estimatedPreparationTimeMinutes
) {
}
