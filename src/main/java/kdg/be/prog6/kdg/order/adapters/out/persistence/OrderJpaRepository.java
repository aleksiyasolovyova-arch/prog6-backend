package kdg.be.prog6.kdg.order.adapters.out.persistence;

import kdg.be.prog6.kdg.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {
    List<OrderEntity> findByRestaurantId(UUID restaurantId);
    List<OrderEntity> findByStatus(OrderStatus status);
}
