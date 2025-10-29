package kdg.be.prog6.kdg.order.core;

import kdg.be.prog6.kdg.order.ports.in.MarkOrderPickedUpCommand;
import kdg.be.prog6.kdg.order.ports.in.MarkOrderPickedUpPort;
import kdg.be.prog6.kdg.order.ports.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MarkOrderPickedUpUseCaseImpl implements MarkOrderPickedUpPort {
    private final OrderRepositoryPort repositoryPort;

    public MarkOrderPickedUpUseCaseImpl(OrderRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    @Transactional
    public void markOrderPickedUp(MarkOrderPickedUpCommand command) {
        var order = repositoryPort.findById(command.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found: " + command.orderId()));

        order.markPickedUp(command.courierId());
        repositoryPort.save(order);
    }
}
