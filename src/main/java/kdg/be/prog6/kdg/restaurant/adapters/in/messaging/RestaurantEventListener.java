package kdg.be.prog6.kdg.restaurant.adapters.in.messaging;

import kdg.be.prog6.kdg.common.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Component
public class RestaurantEventListener {
    private static final Logger log = LoggerFactory.getLogger(RestaurantEventListener.class);

    /**
     * Reacts to events AFTER the publishing transaction commits.
     * Runs asynchronously in a new transaction to decouple from the original operation.
     * This is equivalent to the deprecated @ApplicationModuleListener.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = REQUIRES_NEW)
    public void onRestaurantCreated(RestaurantCreatedEvent event) {
        log.info("Restaurant '{}' created successfully by owner: {}",
                event.restaurantName(), event.ownerId());
        // Side effects here - logging, cache invalidation, notifications
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = REQUIRES_NEW)
    public void onDishPublished(DishPublishedEvent event) {
        log.info("Dish '{}' (ID: {}) published for restaurant: {}",
                event.dishName(), event.dishId(), event.restaurantId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = REQUIRES_NEW)
    public void onDishUnpublished(DishUnpublishedEvent event) {
        log.info("Dish '{}' (ID: {}) unpublished from restaurant: {}",
                event.dishName(), event.dishID(), event.restaurantId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = REQUIRES_NEW)
    public void onDishUpdated(DishUpdatedEvent event) {
        log.info("Dish '{}' (ID: {}) updated for restaurant: {}",
                event.dishName(), event.dishId(), event.restaurantId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = REQUIRES_NEW)
    public void onAllDraftsPublished(AllDraftsPublishedEvent event) {
        log.info("{} drafts published for restaurant: {}",
                event.draftCount(), event.restaurantId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = REQUIRES_NEW)
    public void onRestaurantOpened(RestaurantOpenedEvent event) {
        log.info("Restaurant {} is now OPEN (manual: {})",
                event.restaurantId(), event.isManualOverride());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = REQUIRES_NEW)
    public void onRestaurantClosed(RestaurantClosedEvent event) {
        log.info("Restaurant {} is now CLOSED (manual: {})",
                event.restaurantId(), event.isManualOverride());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = REQUIRES_NEW)
    public void onDishMarkedOutOfStock(DishMarkedOutOfStockEvent event) {
        log.info("Dish {} marked OUT OF STOCK for restaurant: {}",
                event.dishId(), event.restaurantId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = REQUIRES_NEW)
    public void onDishMarkedInStock(DishMarkedInStockEvent event) {
        log.info("Dish {} marked IN STOCK for restaurant: {}",
                event.dishId(), event.restaurantId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = REQUIRES_NEW)
    public void onDraftCreated(DishDraftCreatedEvent event) {
        log.info("Draft created for restaurant: {}, is new dish: {}",
                event.restaurantId(), event.isNewDish());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = REQUIRES_NEW)
    public void onEditStarted(DishEditStartedEvent event) {
        log.info("Editing started for dish {} in restaurant: {}",
                event.dishId(), event.restaurantId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = REQUIRES_NEW)
    public void onDraftDiscarded(DishDraftDiscardedEvent event) {
        log.info("Draft discarded for restaurant: {}", event.restaurantId());
    }
}
