package kdg.be.prog6.kdg.order.core;

import jakarta.transaction.Transactional;
import kdg.be.prog6.kdg.order.domain.Order;
import kdg.be.prog6.kdg.order.domain.exceptions.OrderNotFoundException;
import kdg.be.prog6.kdg.order.domain.exceptions.UnauthorizedException;
import kdg.be.prog6.kdg.order.ports.in.AcceptOrderCommand;
import kdg.be.prog6.kdg.order.ports.in.AcceptOrderPort;
import kdg.be.prog6.kdg.order.ports.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class AcceptOrderUseCaseImpl  implements AcceptOrderPort {
    private final OrderRepositoryPort orderRepository;

    public AcceptOrderUseCaseImpl(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void acceptOrder(AcceptOrderCommand cmd) {
        Order order = orderRepository.findById(cmd.orderId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (!order.getRestaurantId().equals(cmd.restaurantId())) {
            throw new UnauthorizedException("Order does not belong to this restaurant");
        }

        order.accept();
        orderRepository.save(order);
    }
}
