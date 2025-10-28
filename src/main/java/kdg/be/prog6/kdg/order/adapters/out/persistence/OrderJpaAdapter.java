package kdg.be.prog6.kdg.order.adapters.out.persistence;

import kdg.be.prog6.kdg.order.domain.Order;
import kdg.be.prog6.kdg.order.domain.OrderId;
import kdg.be.prog6.kdg.order.ports.out.OrderRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class OrderJpaAdapter implements OrderRepositoryPort {
    private final OrderJpaRepository jpaRepository;
    private final OrderMapper mapper;

    public OrderJpaAdapter(OrderJpaRepository jpaRepository, OrderMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(Order order) {
        OrderEntity entity = mapper.toEntity(order);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return jpaRepository.findById(orderId.uuid())
                .map(mapper::toDomain);
    }

    @Override
    public List<Order> findByRestaurantId(UUID restaurantId) {
        return jpaRepository.findByRestaurantId(restaurantId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
