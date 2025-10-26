package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;

import jakarta.persistence.*;
import kdg.be.prog6.kdg.restaurant.domain.DishDetails;
import kdg.be.prog6.kdg.restaurant.domain.DishType;
import kdg.be.prog6.kdg.restaurant.domain.FoodTag;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Embeddable
public class DishDetailsEmbeddable {
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "dish_type", nullable = false)
    private DishType type;

    // Store food tags as a comma-separated string to avoid FK constraint issues
    @Column(name = "food_tags", length = 500)
    private String foodTagsAsString;

    @Column(length = 1000)
    private String description;

    @Embedded
    private MoneyEmbeddable price;

    @Column(name = "picture_url")
    private String pictureUrl;

    protected DishDetailsEmbeddable() {}

    private DishDetailsEmbeddable(
            String name,
            DishType type,
            String foodTagsAsString,
            String description,
            MoneyEmbeddable price,
            String pictureUrl
    ) {
        this.name = name;
        this.type = type;
        this.foodTagsAsString = foodTagsAsString;
        this.description = description;
        this.price = price;
        this.pictureUrl = pictureUrl;
    }

    // Domain → Persistence
    public static DishDetailsEmbeddable from(DishDetails details) {
        if (details == null) {
            return null;
        }

        // Convert food tags set to comma-separated string
        String foodTagsString = details.foodTags() != null && !details.foodTags().isEmpty()
                ? details.foodTags().stream()
                .map(FoodTag::name)
                .collect(Collectors.joining(","))
                : "";

        return new DishDetailsEmbeddable(
                details.name(),
                details.type(),
                foodTagsString,
                details.description(),
                MoneyEmbeddable.from(details.price()),
                details.pictureUrl()
        );
    }

    // Persistence → Domain
    public DishDetails toDomain() {
        // Convert comma-separated string back to set of FoodTag enums
        Set<FoodTag> foodTags = foodTagsAsString != null && !foodTagsAsString.isEmpty()
                ? Arrays.stream(foodTagsAsString.split(","))
                .map(String::trim)
                .map(FoodTag::valueOf)
                .collect(Collectors.toSet())
                : Set.of();

        return new DishDetails(
                name,
                type,
                foodTags,
                description,
                price.toDomain(),
                pictureUrl
        );
    }

    // Getters for JPA
    public String getName() { return name; }
    public DishType getType() { return type; }
    public String getFoodTagsAsString() { return foodTagsAsString; }
    public String getDescription() { return description; }
    public MoneyEmbeddable getPrice() { return price; }
    public String getPictureUrl() { return pictureUrl; }
}