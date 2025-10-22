package kdg.be.prog6.kdg.restaurant.domain;

import java.util.HashSet;
import java.util.Set;

public class Dish {
    private RestaurantId restaurantId;
    private DishId id;
    private String name;
    private DishType type;
    private Money price;
    private String description;
    private String pictureUrl;

    public DishId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DishType getType() {
        return type;
    }

    public Money getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public Set<FoodTag> getFoodTags() {
        return foodTags;
    }

    public boolean isInStock() {
        return inStock;
    }

    private Set<FoodTag> foodTags;
    private boolean inStock = true;

    public void markAsOutOfStock() {
        this.inStock = false;
    }

    public void markInStock() {
        this.inStock = true;
    }

    public boolean isAvailableForOrder() {
        return inStock;
    }
    public RestaurantId getRestaurantId() {
        return restaurantId;
    }


    void applyChanges(DishDetails details){
        this.name = details.name();
        this.type = details.type();
        this.price = details.price();
        this.description = details.description();
        this.pictureUrl = details.pictureUrl();
        this.foodTags = new HashSet<>(details.foodTags());
    }
}
