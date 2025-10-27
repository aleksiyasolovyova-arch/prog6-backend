package kdg.be.prog6.kdg.restaurant.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Dish {
    private DishId id;
    private RestaurantId restaurantId;
    private DishDetails details;  // ← Wrapped in value object
    private boolean availableForOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Dish() {}

    public static Dish create(
            DishId id,
            RestaurantId restaurantId,
            DishDetails details
    ) {
        Dish dish = new Dish();
        dish.id = id;
        dish.restaurantId = restaurantId;
        dish.details = details;
        dish.availableForOrder = true;
        dish.createdAt = LocalDateTime.now();
        dish.updatedAt = LocalDateTime.now();
        return dish;
    }

    public static Dish reconstitute(
            DishId id,
            RestaurantId restaurantId,
            DishDetails details,
            boolean availableForOrder,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        Dish dish = new Dish();
        dish.id = id;
        dish.restaurantId = restaurantId;
        dish.details = details;
        dish.availableForOrder = availableForOrder;
        dish.createdAt = createdAt;
        dish.updatedAt = updatedAt;
        return dish;
    }


    // Update all details at once
    public void updateDetails(DishDetails newDetails) {
        this.details = newDetails;
        this.updatedAt = LocalDateTime.now();
    }

    // Availability management (separate from details)
    public void markAsOutOfStock() {
        this.availableForOrder = false;
    }

    public void markInStock() {
        this.availableForOrder = true;
    }

    public boolean isAvailableForOrder() {
        return availableForOrder;
    }

    // Getters
    public DishId getId() { return id; }
    public RestaurantId getRestaurantId() { return restaurantId; }
    public DishDetails getDetails() { return details; }

    // Convenience methods (optional - delegate to details)
    public String getName() { return details.name(); }
    public Money getPrice() { return details.price(); }
    public DishType getType() { return details.type(); }
    public Set<FoodTag> getFoodTags() {return details.foodTags();}
    public String getDescription() { return details.description(); }
    public String getPictureUrl() { return details.pictureUrl(); }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
