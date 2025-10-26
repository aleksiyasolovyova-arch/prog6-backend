package kdg.be.prog6.kdg.restaurant.adapters.in.request;

import kdg.be.prog6.kdg.restaurant.domain.DishDetails;
import kdg.be.prog6.kdg.restaurant.domain.DishType;
import kdg.be.prog6.kdg.restaurant.domain.FoodTag;
import kdg.be.prog6.kdg.restaurant.domain.Money;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UpdateDishDraftRequest(
        UUID restaurantId,
        String name,
        String type,
        Set<String> foodTags,
        String description,
        BigDecimal priceAmount,
        String priceCurrency,
        String pictureUrl
) {
    public DishDetails toDetails() {
        Set<FoodTag> tags = foodTags != null
                ? foodTags.stream()
                .map(tag -> FoodTag.valueOf(tag.toUpperCase()))
                .collect(Collectors.toSet())
                : Set.of();

        return new DishDetails(
                name,
                DishType.valueOf(type.toUpperCase()),
                tags,
                description,
                Money.of(priceAmount, priceCurrency),
                pictureUrl
        );
    }
}
