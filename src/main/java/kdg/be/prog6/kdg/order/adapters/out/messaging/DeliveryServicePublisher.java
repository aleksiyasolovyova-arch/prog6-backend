package kdg.be.prog6.kdg.order.adapters.out.messaging;

import kdg.be.prog6.kdg.common.events.OrderReadyForPickupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class DeliveryServicePublisher {
    private static final Logger log = LoggerFactory.getLogger(DeliveryServicePublisher.class);
    private static final String EXCHANGE = "kdg.orders";
    private static final String ROUTING_KEY_READY = "order.ready.v1";

    private final RabbitTemplate rabbitTemplate;

    public DeliveryServicePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Publish order ready event to delivery service
     * The Jackson2JsonMessageConverter will automatically serialize to JSON
     */
    public void publishOrderReady(OrderReadyForPickupEvent event) {
        try {
            // rabbitTemplate now converts the event to JSON automatically
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY_READY, event);
            log.info("Published OrderReadyForPickupEvent for order {} to delivery service", event.orderId());
        } catch (Exception e) {
            log.error("Failed to publish order ready event for order {}", event.orderId(), e);
            throw new RuntimeException("Failed to notify delivery service", e);
        }
    }
}
