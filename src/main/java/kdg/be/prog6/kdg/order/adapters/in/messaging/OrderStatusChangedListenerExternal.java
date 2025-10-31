package kdg.be.prog6.kdg.order.adapters.in.messaging;

import kdg.be.prog6.kdg.common.config.RabbitMQSetup;
import kdg.be.prog6.kdg.common.events.*;
import kdg.be.prog6.kdg.order.core.MarkOrderReadyUseCaseImpl;
import kdg.be.prog6.kdg.order.ports.in.OrderStatusProjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import static org.springframework.amqp.support.AmqpHeaders.CONSUMER_QUEUE;
import static org.springframework.amqp.support.AmqpHeaders.RECEIVED_ROUTING_KEY;

@Component
public class OrderStatusChangedListenerExternal {
    private final OrderStatusProjector projector;

    private static final Logger log = LoggerFactory.getLogger(OrderStatusChangedListenerExternal.class);


    public OrderStatusChangedListenerExternal(OrderStatusProjector projector) {
        this.projector = projector;
    }

    /**
     * When restaurant accepts order → update read model
     */
    @RabbitListener(queues = RabbitMQSetup.QUEUE_ORDER_ACCEPTED)
    public void onOrderAccepted(
            OrderAcceptedEvent event,
            @Header(CONSUMER_QUEUE) String queue,
            @Header(RECEIVED_ROUTING_KEY) String routingKey
    ) {
        try {
            log.info("📨 [{}] Received ACCEPTED event for orderId={}", queue, event.orderId());
            log.debug("   Routing key: {}", routingKey);

            projector.projectOrderAccepted(event.orderId());

            log.debug("✅ Projected ACCEPTED for orderId={}", event.orderId());
        } catch (Exception e) {
            log.error("❌ Failed to project ACCEPTED event for orderId={}", event.orderId(), e);
            throw e;  // Requeue the message
        }
    }

    /**
     * When order is ready for pickup → update read model
     */
    @RabbitListener(queues = RabbitMQSetup.QUEUE_ORDER_READY)
    public void onOrderReadyForPickup(
            OrderReadyForPickupEvent event,
            @Header(CONSUMER_QUEUE) String queue,
            @Header(RECEIVED_ROUTING_KEY) String routingKey
    ) {
        try {
            log.info("📨 [{}] Received READY event for orderId={}", queue, event.orderId());
            log.debug("   Routing key: {}", routingKey);

            projector.projectOrderReady(event.orderId());

            log.debug("✅ Projected READY for orderId={}", event.orderId());
        } catch (Exception e) {
            log.error("❌ Failed to project READY event for orderId={}", event.orderId(), e);
            throw e;  // Requeue the message
        }
    }

    /**
     * When delivery service picks up order → update read model
     */
    @RabbitListener(queues = RabbitMQSetup.QUEUE_ORDER_PICKED)
    public void onOrderPickedUp(OrderPickedUpEvent event) {
        try {
            log.info("📨 Received PICKED_UP event for orderId={}", event.orderId());

            projector.projectOrderPickedUp(event.orderId());

            log.debug("✅ Projected PICKED_UP for orderId={}", event.orderId());
        } catch (Exception e) {
            log.error("❌ Failed to project PICKED_UP event for orderId={}", event.orderId(), e);
            throw e;  // Requeue the message
        }
    }

    /**
     * When delivery service updates location → update read model
     */
    @RabbitListener(queues = RabbitMQSetup.QUEUE_ORDER_LOCATION)
    public void onOrderLocationUpdate(OrderLocationEvent event) {
        try {
            if (event == null) {
                log.warn("⚠️ Received null OrderLocationEvent, skipping");
                return;
            }

            var location = event.location();

            // Skip if coordinates are invalid
            if (location == null || location.lat() == null || location.lng() == null) {
                log.debug("⚠️ Skipping location update with null coordinates for orderId={}", event.orderId());
                return;
            }

            log.debug("📍 Updating location for orderId={} to lat={}, lng={}",
                    event.orderId(), location.lat(), location.lng());

            projector.projectOrderLocation(
                    event.orderId(),
                    location.lat(),
                    location.lng()
            );

            log.debug("✅ Projected location for orderId={}", event.orderId());
        } catch (Exception e) {
            log.error("❌ Failed to project location event for orderId={}",
                    event != null ? event.orderId() : "unknown", e);
            throw e;  // Requeue the message
        }
    }

    /**
     * When order is delivered → update read model
     */
    @RabbitListener(queues = RabbitMQSetup.QUEUE_ORDER_DELIVERED)
    public void onOrderDelivered(OrderDeliveredEvent event) {
        try {
            log.info("📨 Received DELIVERED event for orderId={}", event.orderId());

            projector.projectOrderDelivered(event.orderId());

            log.debug("✅ Projected DELIVERED for orderId={}", event.orderId());
        } catch (Exception e) {
            log.error("❌ Failed to project DELIVERED event for orderId={}", event.orderId(), e);
            throw e;  // Requeue the message
        }
    }
}
