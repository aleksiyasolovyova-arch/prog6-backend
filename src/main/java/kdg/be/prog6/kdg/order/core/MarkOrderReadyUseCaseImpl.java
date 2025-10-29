package kdg.be.prog6.kdg.order.core;

import kdg.be.prog6.kdg.order.domain.Order;
import kdg.be.prog6.kdg.order.domain.exceptions.OrderNotFoundException;
import kdg.be.prog6.kdg.order.domain.exceptions.UnauthorizedRestaurantException;
import kdg.be.prog6.kdg.order.ports.in.MarkOrderReadyCommand;
import kdg.be.prog6.kdg.order.ports.in.MarkOrderReadyPort;
import kdg.be.prog6.kdg.order.ports.out.OrderRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MarkOrderReadyUseCaseImpl implements MarkOrderReadyPort {

    private static final Logger log = LoggerFactory.getLogger(MarkOrderReadyUseCaseImpl.class);
    private final OrderRepositoryPort repositoryPort;

    public MarkOrderReadyUseCaseImpl(OrderRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    @Transactional
    public void markOrderReady(MarkOrderReadyCommand command) {
        log.info("Marking order {} as ready for pickup (restaurant: {})",
                command.orderId(), command.restaurantId());

        // Fetch the order
        Order order = repositoryPort.findById(command.orderId())
                .orElseThrow(() -> {
                    log.error("Order not found: {}", command.orderId());
                    return new OrderNotFoundException("Order not found: " + command.orderId());
                });

        // Verify restaurant owns this order
        if (!order.getRestaurantId().equals(command.restaurantId())) {
            log.error("Restaurant {} attempted to mark order {} as ready, but order belongs to restaurant {}",
                    command.restaurantId(), command.orderId(), order.getRestaurantId());
            throw new UnauthorizedRestaurantException(
                    "Restaurant does not own this order"
            );
        }
        order.markReady();
        repositoryPort.save(order);

        log.info("Order {} marked as ready for pickup", command.orderId());
    }
}
