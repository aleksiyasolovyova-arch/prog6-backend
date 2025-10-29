package kdg.be.prog6.kdg.order.core;

import kdg.be.prog6.kdg.order.adapters.in.response.OrderResponse;
import kdg.be.prog6.kdg.order.domain.Order;
import kdg.be.prog6.kdg.order.domain.OrderId;
import kdg.be.prog6.kdg.order.domain.exceptions.OrderNotFoundException;
import kdg.be.prog6.kdg.order.ports.in.GetOrderDetailsPort;
import kdg.be.prog6.kdg.order.ports.out.OrderRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

public class GetOrderDetailsUseCaseImpl implements GetOrderDetailsPort {
    private final OrderRepositoryPort orderRepository;

    public GetOrderDetailsUseCaseImpl(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderDetails(OrderId orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        return OrderResponse.mapToResponse(order);
    }
}
