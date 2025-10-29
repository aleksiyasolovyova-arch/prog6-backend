package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;

import kdg.be.prog6.kdg.restaurant.domain.DishDraft;
import kdg.be.prog6.kdg.restaurant.domain.DraftId;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;
import kdg.be.prog6.kdg.restaurant.domain.DishId;
import org.springframework.stereotype.Component;

@Component
public class DishDraftMapper {

    /**
     * Converts a domain DishDraft to a JPA DishDraftEntity.
     * This method is used when persisting a draft to the database.
     */
    public DishDraftEntity toEntity(DishDraft draft) {
        if (draft == null) {
            return null;
        }

        DishDraftEntity entity = new DishDraftEntity();
        entity.setId(draft.getId().uuid());
        entity.setRestaurantId(draft.getRestaurantId().uuid());

        if (draft.getOriginalDishId() != null) {
            entity.setOriginalDishId(draft.getOriginalDishId().uuid());
        }

        entity.setDetails(DishDetailsEmbeddable.from(draft.getDetails()));
        entity.setIsNewDish(draft.isNewDish());
        entity.setCreatedAt(draft.getCreatedAt());
        entity.setUpdatedAt(draft.getUpdatedAt());

        entity.setScheduledPublishAt(draft.getScheduledPublishAt());
        entity.setIsScheduled(draft.isScheduled());

        return entity;
    }

    /**
     * Converts a JPA DishDraftEntity to a domain DishDraft.
     * This method is used when fetching a draft from the database.
     */
    public DishDraft toDomain(DishDraftEntity entity) {
        if (entity == null) {
            return null;
        }

        DishDraft draft = DishDraft.reconstitute(
                DraftId.from(entity.getId()),
                RestaurantId.from(entity.getRestaurantId()),
                entity.getOriginalDishId() != null
                        ? DishId.from(entity.getOriginalDishId())
                        : null,
                entity.getDetails().toDomain(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );

        // NEW - Restore scheduling state without validation
        // (The validation already happened when it was first scheduled)
        if (entity.isScheduled() && entity.getScheduledPublishAt() != null) {
            draft.restoreScheduledState(entity.getScheduledPublishAt());
        }

        return draft;
    }
}
