package kdg.be.prog6.kdg.restaurant.domain;

import kdg.be.prog6.kdg.restaurant.domain.exceptions.InvalidDraftStateException;
import kdg.be.prog6.kdg.restaurant.domain.exceptions.InvalidPublishingException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class DishDraft {
     private DraftId id;
     private RestaurantId restaurantId;
     private DishId originalDishId; //null for new dishes obv

    private String name;
    private DishType type;
    private Money price;
    private String description;
    private String pictureUrl;
    private Set<FoodTag> foodTags;

    private DraftState state;
    private LocalDateTime scheduledPublishAt;
    private LocalDateTime createdAt;


    //Create a draft for a new dish using the Factory method
    public static DishDraft createNew(DraftId id, RestaurantId restaurantId, DishDetails details) {
        DishDraft draft = new DishDraft();
        draft.id = id;
        draft.restaurantId = restaurantId;
        draft.originalDishId = null;
        draft.applyDetails(details);
        draft.state = DraftState.DRAFT;
        draft.createdAt = LocalDateTime.now();
        return draft;
    }

    //Create a draft from an existing dish using the Factory method
    public static DishDraft createFromPublished(DraftId id, Dish publishedDish) {
        DishDraft draft = new DishDraft();
        draft.id = id;
        draft.restaurantId = publishedDish.getRestaurantId();
        draft.originalDishId = publishedDish.getId(); // Link to original

        // Copy current published values as draft starting point
        draft.name = publishedDish.getName();
        draft.type = publishedDish.getType();
        draft.price = publishedDish.getPrice();
        draft.description = publishedDish.getDescription();
        draft.pictureUrl = publishedDish.getPictureUrl();
        draft.foodTags = new HashSet<>(publishedDish.getFoodTags());

        draft.state = DraftState.DRAFT;
        draft.createdAt = LocalDateTime.now();
        return draft;
    }


    public boolean isNewDish() {
        return originalDishId == null;
    }

    public void editDetails(DishDetails details) {
        validateState();
        applyDetails(details);
    }

    public void schedulePublish(LocalDateTime publishAt) {
        if (publishAt.isBefore(LocalDateTime.now())) {
            throw new InvalidPublishingException("Publish time must be in the future");
        }
        this.scheduledPublishAt = publishAt;
        this.state = DraftState.SCHEDULED;
    }

    public Dish toPublishedDish() {
        if (!isNewDish()) {
            throw new IllegalStateException("Cannot convert draft to published dish: " + id);
        }
        return Dish.create(
              DishId.generate(),
              restaurantId,
              name, type, foodTags, price, description, pictureUrl
        );
    }

    public void applyChangesTo(Dish existingDish) {
        if(isNewDish()) {
            throw new IllegalStateException("Cannot apply draft changes to published dish: " + id);
        }
        existingDish.applyChanges(new DishDetails(name, type, foodTags, description, price, pictureUrl));
    }

    private void applyDetails(DishDetails details) {
        validateDetails(details);
        this.name = details.name();
        this.type = details.type();
        this.foodTags = new HashSet<>(details.foodTags());
        this.description = details.description();
        this.price = details.price();
        this.pictureUrl = details.pictureUrl();
    }

    private void validateDetails(DishDetails details) {
        if (details.name() == null || details.type() == null || details.price() == null) {
            throw new IllegalArgumentException("Name, type and price are required");
        }
        if (details.name().length() < 3 || details.name().length() > 50) {
            throw new IllegalArgumentException("Name must be between 3 and 50 characters");
        }
        if (details.description() != null && (details.description().length() < 10 || details.description().length() > 1000)) {
            throw new IllegalArgumentException("Description must be between 10 and 1000 characters");
        }
        if (price.amount().compareTo(BigDecimal.valueOf(0.01)) < 0 || price.amount().compareTo(BigDecimal.valueOf(1000.00)) > 0) {
            throw new IllegalArgumentException("Price must be between 0.01 and 1000.00");
        }

        if (details.pictureUrl() != null && !details.pictureUrl().matches("^https?://.*\\.(jpg|jpeg|png|gif)$")) {
            throw new IllegalArgumentException("Invalid picture URL format");
        }
    }

    private void validateState() {
        if (state == DraftState.REJECTED) {
            throw new InvalidDraftStateException("Cannot edit rejected draft");
        }
    }

    public DraftId getId() {
        return id;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public DishId getOriginalDishId() {
        return originalDishId;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public DishType getType() {
        return type;
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

    public DraftState getState() {
        return state;
    }

    public LocalDateTime getScheduledPublishAt() {
        return scheduledPublishAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isScheduledFor(LocalDateTime currentTime) {
        return state == DraftState.SCHEDULED &&
                scheduledPublishAt != null &&
                scheduledPublishAt.equals(currentTime);
    }
}
