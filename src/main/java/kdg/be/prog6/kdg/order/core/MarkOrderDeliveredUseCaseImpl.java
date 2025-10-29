package kdg.be.prog6.kdg.order.core;

import kdg.be.prog6.kdg.order.ports.in.MarkOrderDeliveredCommand;
import kdg.be.prog6.kdg.order.ports.in.MarkOrderDeliveredPort;
import kdg.be.prog6.kdg.order.ports.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MarkOrderDeliveredUseCaseImpl implements MarkOrderDeliveredPort {

    private final OrderRepositoryPort repositoryPort;

    public MarkOrderDeliveredUseCaseImpl(OrderRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    @Transactional
    public void markOrderDelivered(MarkOrderDeliveredCommand command) {
        var order = repositoryPort.findById(command.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found: " + command.orderId()));

        order.markDelivered();
        repositoryPort.save(order);
    }
}
