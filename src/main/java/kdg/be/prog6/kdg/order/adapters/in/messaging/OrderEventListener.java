package kdg.be.prog6.kdg.order.adapters.in.messaging;

import kdg.be.prog6.kdg.common.events.*;
import kdg.be.prog6.kdg.order.adapters.out.messaging.DeliveryServicePublisher;
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
    private final DeliveryServicePublisher deliveryServicePublisher;

    public OrderEventListener(DeliveryServicePublisher deliveryServicePublisher) {
        this.deliveryServicePublisher = deliveryServicePublisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onOrderPlaced(OrderPlacedEvent event) {
        log.info("Order {} placed - waiting for restaurant decision", event.orderId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onOrderAccepted(OrderAcceptedEvent event) {
        log.info("Order {} accepted - kitchen starting preparation", event.orderId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onOrderRejected(OrderRejectedEvent event) {
        log.info("Order {} rejected - reason: {}", event.orderId(), event.rejectionReason());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onOrderReadyForPickup(OrderReadyForPickupEvent event) {
        log.info("Order {} ready for pickup - notifying delivery service", event.orderId());

        try {
            deliveryServicePublisher.publishOrderReady(event);
        } catch (Exception e) {
            log.error("Failed to notify delivery service for order {}", event.orderId(), e);
            // In production, retry or log to a dead letter queue
        }
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
