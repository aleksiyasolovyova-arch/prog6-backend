package kdg.be.prog6.kdg.order.core;

import kdg.be.prog6.kdg.order.domain.Order;
import kdg.be.prog6.kdg.order.domain.exceptions.OrderNotFoundException;
import kdg.be.prog6.kdg.order.domain.exceptions.UnauthorizedException;
import kdg.be.prog6.kdg.order.ports.in.RejectOrderCommand;
import kdg.be.prog6.kdg.order.ports.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RejectOrderUseCaseImpl {
    private final OrderRepositoryPort orderRepository;

    public RejectOrderUseCaseImpl(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void rejectOrder(RejectOrderCommand command){
        Order order = orderRepository.findById(command.orderId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (!order.getRestaurantId().equals(command.restaurantId())) {
            throw new UnauthorizedException("Order does not belong to this restaurant");
        }

        order.reject();
        orderRepository.save(order);
    }

}
