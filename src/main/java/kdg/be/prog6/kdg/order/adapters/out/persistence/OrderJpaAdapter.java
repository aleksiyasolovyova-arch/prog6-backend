package kdg.be.prog6.kdg.order.adapters.out.persistence;

import kdg.be.prog6.kdg.order.domain.Order;
import kdg.be.prog6.kdg.order.domain.OrderId;
import kdg.be.prog6.kdg.order.domain.OrderStatus;
import kdg.be.prog6.kdg.order.ports.out.OrderRepositoryPort;
import org.jmolecules.event.types.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class OrderJpaAdapter implements OrderRepositoryPort {
    private final OrderJpaRepository jpaRepository;
    private final OrderMapper mapper;
    private final ApplicationEventPublisher eventPublisher;

    public OrderJpaAdapter(OrderJpaRepository jpaRepository, OrderMapper mapper, ApplicationEventPublisher eventPublisher) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void save(Order order) {
        OrderEntity entity = mapper.toEntity(order);
        jpaRepository.save(entity);
        publishDomainEvents(order);
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

    private void publishDomainEvents(Order order) {
        List<DomainEvent> events = order.getDomainEvents();

        for (DomainEvent event : events) {
            eventPublisher.publishEvent(event);
        }

        order.clearDomainEvents();
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return jpaRepository.findByStatus(status).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
