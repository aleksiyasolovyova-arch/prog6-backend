package kdg.be.prog6.kdg.restaurant.domain;

import kdg.be.prog6.kdg.restaurant.domain.exceptions.InvalidDraftStateException;
import kdg.be.prog6.kdg.restaurant.domain.exceptions.InvalidPublishingException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

public class DishDraft {
     private DraftId id;
     private RestaurantId restaurantId;
     private DishId originalDishId; //null for new dishes obv

   private DishDetails details;

    private DraftState state;
    private LocalDateTime scheduledPublishAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    //Create a draft for a new dish using the Factory method
    public static DishDraft createNew(
            DraftId draftId,
            RestaurantId restaurantId,
            DishDetails details
    ) {
        DishDraft draft = new DishDraft();
        draft.id = draftId;
        draft.restaurantId = restaurantId;
        draft.originalDishId = null;
        draft.details = details;
        draft.createdAt = LocalDateTime.now();
        draft.updatedAt = LocalDateTime.now();
        return draft;
    }

    //Create a draft from an existing dish using the Factory method
    public static DishDraft createFromPublished(DraftId id, Dish publishedDish) {
        DishDraft draft = new DishDraft();
        draft.id = id;
        draft.restaurantId = publishedDish.getRestaurantId();
        draft.originalDishId = publishedDish.getId(); // Link to original

        draft.state = DraftState.DRAFT;
        draft.details = publishedDish.getDetails();  // ← Reuse details
        draft.createdAt = LocalDateTime.now();
        draft.updatedAt = LocalDateTime.now();
        return draft;
    }

    //Reconstitute a draft from database/persistence layer
    public static DishDraft reconstitute(
            DraftId id,
            RestaurantId restaurantId,
            DishId originalDishId,  // nullable
            DishDetails details,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        DishDraft draft = new DishDraft();
        draft.id = id;
        draft.restaurantId = restaurantId;
        draft.originalDishId = originalDishId;  // null for new dishes
        draft.details = details;
        draft.createdAt = createdAt;
        draft.updatedAt = updatedAt;
        return draft;
    }



    public void editDetails(DishDetails newDetails) {
        this.details = newDetails;
        this.updatedAt = LocalDateTime.now();
    }

    public Dish toPublishedDish() {
        return Dish.create(
                DishId.generate(),
                restaurantId,
                details
        );
    }

    public void applyChangesTo(Dish existingDish) {
        existingDish.updateDetails(this.details);
    }

    // Getters
    public DraftId getId() { return id; }
    public RestaurantId getRestaurantId() { return restaurantId; }
    public DishId getOriginalDishId() { return originalDishId; }
    public DishDetails getDetails() { return details; }
    public boolean isNewDish() { return originalDishId == null; }

    public DraftState getState() {
        return state;
    }

    public LocalDateTime getScheduledPublishAt() {
        return scheduledPublishAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}