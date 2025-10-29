package kdg.be.prog6.kdg.order.adapters.in.messaging;

import kdg.be.prog6.kdg.common.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderEventListener {
    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onOrderPlaced(OrderPlacedEvent event) {
        log.info("Order {} placed for restaurant {} - total: {}",
                event.orderId(), event.restaurantId(), event.totalAmount());
        // TODO: Notify restaurant via RabbitMQ
        // TODO: Start 5-minute decision deadline timer
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onOrderAccepted(OrderAcceptedEvent event) {
        log.info("Order {} accepted at {}", event.orderId(), event.acceptedAt());
        // TODO: Notify delivery service via RabbitMQ
        // TODO: Update order kitchen status
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onOrderRejected(OrderRejectedEvent event) {
        log.info("Order {} rejected - reason: {}", event.orderId(), event.rejectionReason());
        // TODO: Notify customer
        // TODO: Release reservation
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onOrderReadyForPickup(OrderReadyForPickupEvent event) {
        log.info("Order {} ready for pickup", event.orderId());
        // TODO: Notify delivery service via RabbitMQ to collect order
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onOrderDelivered(OrderDeliveredEvent event) {
        log.info("Order {} delivered", event.orderId());
        // TODO: Mark order complete in UI
        // TODO: Update customer status
    }
}
