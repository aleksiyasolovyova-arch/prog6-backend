package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "dish_drafts")
public class DishDraftEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "restaurant_id", columnDefinition = "uuid", nullable = false)
    private UUID restaurantId;

    @Column(name = "original_dish_id", columnDefinition = "uuid")
    private UUID originalDishId;  // NULL if new dish

    @Embedded
    private DishDetailsEmbeddable details;

    @Column(name = "is_new_dish", nullable = false)
    private boolean isNewDish;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "scheduled_publish_at")
    private LocalDateTime scheduledPublishAt;

    @Column(name = "is_scheduled")
    private boolean isScheduled;

    protected DishDraftEntity() {}

    // Getters, setters...
    public UUID getId() { return id; }
    public UUID getRestaurantId() { return restaurantId; }
    public UUID getOriginalDishId() { return originalDishId; }
    public DishDetailsEmbeddable getDetails() { return details; }
    public boolean isNewDish() { return isNewDish; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(UUID id) { this.id = id; }
    public void setRestaurantId(UUID restaurantId) { this.restaurantId = restaurantId; }
    public void setOriginalDishId(UUID originalDishId) { this.originalDishId = originalDishId; }
    public void setDetails(DishDetailsEmbeddable details) { this.details = details; }
    public void setIsNewDish(boolean newDish) { isNewDish = newDish; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getScheduledPublishAt() {
        return scheduledPublishAt;
    }

    public void setScheduledPublishAt(LocalDateTime scheduledPublishAt) {
        this.scheduledPublishAt = scheduledPublishAt;
    }

    public void setNewDish(boolean newDish) {
        isNewDish = newDish;
    }

    public boolean isScheduled() {
        return isScheduled;
    }

    public void setIsScheduled(boolean scheduled) {
        isScheduled = scheduled;
    }
}
