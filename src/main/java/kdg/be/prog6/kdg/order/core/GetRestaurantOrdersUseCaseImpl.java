package kdg.be.prog6.kdg.order.core;

import kdg.be.prog6.kdg.order.adapters.in.response.OrderResponse;
import kdg.be.prog6.kdg.order.domain.DeliveryAddress;
import kdg.be.prog6.kdg.order.domain.Order;
import kdg.be.prog6.kdg.order.domain.OrderLine;
import kdg.be.prog6.kdg.order.ports.in.GetRestaurantOrdersPort;
import kdg.be.prog6.kdg.order.ports.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GetRestaurantOrdersUseCaseImpl implements GetRestaurantOrdersPort {

    private final OrderRepositoryPort orderRepository;

    public GetRestaurantOrdersUseCaseImpl(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getRestaurantOrders(UUID restaurantId) {
        List<Order> orders = orderRepository.findByRestaurantId(restaurantId);

        return orders.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId().uuid(),
                order.getCustomerInfo().name(),
                order.getCustomerInfo().email(),
                mapAddress(order.getCustomerInfo().address()),
                order.getRestaurantId(),
                order.getRestaurantName(),
                order.getOrderLines().stream()
                        .map(this::mapOrderLine)
                        .toList(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getOrderedAt().toString(),
                order.getEstimatedReadyAt() != null ? order.getEstimatedReadyAt().toString() : null
        );
    }

    private OrderResponse.OrderLineResponse mapOrderLine(OrderLine line) {
        return new OrderResponse.OrderLineResponse(
                line.getDishId(),
                line.getDishName(),
                line.getPrice(),
                line.getQuantity(),
                line.getTotalPrice()
        );
    }

    private OrderResponse.DeliveryAddressResponse mapAddress(DeliveryAddress address) {
        return new OrderResponse.DeliveryAddressResponse(
                address.street(),
                address.number(),
                address.postalCode(),
                address.city(),
                address.country()
        );
    }

}
