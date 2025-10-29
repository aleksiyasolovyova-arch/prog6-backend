package kdg.be.prog6.kdg.order.adapters.in.messaging;

import kdg.be.prog6.kdg.order.domain.OrderId;
import kdg.be.prog6.kdg.order.ports.in.MarkOrderDeliveredCommand;
import kdg.be.prog6.kdg.order.ports.in.MarkOrderDeliveredPort;
import kdg.be.prog6.kdg.order.ports.in.MarkOrderPickedUpCommand;
import kdg.be.prog6.kdg.order.ports.in.MarkOrderPickedUpPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DeliveryServiceListener {
    private static final Logger log = LoggerFactory.getLogger(DeliveryServiceListener.class);

    private final MarkOrderPickedUpPort markOrderPickedUpService;
    private final MarkOrderDeliveredPort markOrderDeliveredService;

    public DeliveryServiceListener(
            MarkOrderPickedUpPort markOrderPickedUpService,
            MarkOrderDeliveredPort markOrderDeliveredService) {
        this.markOrderPickedUpService = markOrderPickedUpService;
        this.markOrderDeliveredService = markOrderDeliveredService;
    }

    /**
     * Listen for "order.picked-up.v1" from delivery service
     * Delivery service has picked up the order
     */
    @RabbitListener(queues = "kdg.orders.picked-up")
    public void handleOrderPickedUp(OrderPickedUpMessage message) {
        log.info("Received order picked-up notification from delivery service: {}", message.orderId());

        try {
            // Update order status in our system
            markOrderPickedUpService.markOrderPickedUp(
                    new MarkOrderPickedUpCommand(
                            OrderId.from(message.orderId()),
                            message.restaurantId(),
                            message.courierId()
                    )
            );
            log.info("Order {} marked as picked up by courier {}", message.orderId(), message.courierId());
        } catch (Exception e) {
            log.error("Failed to mark order {} as picked up", message.orderId(), e);
            throw e;  // Requeue the message on failure
        }
    }

    /**
     * Listen for "order.delivered.v1" from delivery service
     * Delivery service has delivered the order to customer
     */
    @RabbitListener(queues = "kdg.orders.delivered")
    public void handleOrderDelivered(OrderDeliveredMessage message) {
        log.info("Received order delivered notification from delivery service: {}", message.orderId());

        try {
            // Update order status to DELIVERED
            markOrderDeliveredService.markOrderDelivered(
                    new MarkOrderDeliveredCommand(
                            OrderId.from(message.orderId()),
                            message.restaurantId()
                    )
            );
            log.info("Order {} marked as delivered", message.orderId());
        } catch (Exception e) {
            log.error("Failed to mark order {} as delivered", message.orderId(), e);
            throw e;  // Requeue the message on failure
        }
    }
}
