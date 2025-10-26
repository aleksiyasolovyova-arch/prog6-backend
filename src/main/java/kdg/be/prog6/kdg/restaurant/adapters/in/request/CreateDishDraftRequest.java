package kdg.be.prog6.kdg.restaurant.adapters.in.request;

import kdg.be.prog6.kdg.restaurant.domain.*;
import kdg.be.prog6.kdg.restaurant.ports.in.CreateDishDraftCommand;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record CreateDishDraftRequest(String restaurantId,
                                     String name,
                                     String type,
                                     Set<String> foodTags,
                                     String description,
                                     BigDecimal priceAmount,
                                     String priceCurrency,
                                     String pictureUrl
) {

    public CreateDishDraftCommand toCommand() {
        // Convert tag names to enum
        Set<FoodTag> tags = foodTags != null
                ? foodTags.stream()
                .map(tag -> FoodTag.valueOf(tag.toUpperCase()))
                .collect(java.util.stream.Collectors.toSet())
                : Set.of();

        DishDetails details = new DishDetails(
                name,
                DishType.valueOf(type.toUpperCase()),
                tags,
                description,
                Money.of(priceAmount, priceCurrency),
                pictureUrl
        );

        return new CreateDishDraftCommand(
                RestaurantId.from(UUID.fromString(restaurantId)),
                details
        );
    }
}
