package kdg.be.prog6.kdg.order.ports.out;

import kdg.be.prog6.kdg.order.domain.Order;
import kdg.be.prog6.kdg.order.domain.OrderId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {
    void save(Order order);
    Optional<Order> findById(OrderId orderId);
    List<Order> findByRestaurantId(UUID restaurantId);
}
