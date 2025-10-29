package kdg.be.prog6.kdg.order.adapters.in.messaging;

import kdg.be.prog6.kdg.common.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderRestaurantEventListener {
    private static final Logger log = LoggerFactory.getLogger(OrderRestaurantEventListener.class);

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public void onRestaurantOpened(RestaurantOpenedEvent event) {
        log.info("Order context: Restaurant {} opened - accepting orders",
                event.restaurantId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public void onRestaurantClosed(RestaurantClosedEvent event) {
        log.info("Order context: Restaurant {} closed - stop accepting orders",
                event.restaurantId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public void onDishPublished(DishPublishedEvent event) {
        log.info("Order context: Dish '{}' available for ordering from restaurant: {}",
                event.dishName(), event.restaurantId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public void onDishUnpublished(DishUnpublishedEvent event) {
        log.info("Order context: Dish '{}' no longer available from restaurant: {}",
                event.dishName(), event.restaurantId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public void onDishMarkedOutOfStock(DishMarkedOutOfStockEvent event) {
        log.info("Order context: Dish {} out of stock in restaurant: {}",
                event.dishId(), event.restaurantId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public void onDishMarkedInStock(DishMarkedInStockEvent event) {
        log.info("Order context: Dish {} back in stock in restaurant: {}",
                event.dishId(), event.restaurantId());
    }
}
