package kdg.be.prog6.kdg.order.core;

import kdg.be.prog6.kdg.order.adapters.in.messaging.OrderStatusChangedListenerExternal;
import kdg.be.prog6.kdg.order.domain.Order;
import kdg.be.prog6.kdg.order.domain.OrderId;
import kdg.be.prog6.kdg.order.domain.OrderStatus;
import kdg.be.prog6.kdg.order.ports.in.OrderStatusProjector;
import kdg.be.prog6.kdg.order.ports.out.OrderRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class OrderStatusProjectorImpl implements OrderStatusProjector {

    private static final Logger log = LoggerFactory.getLogger(OrderStatusProjectorImpl.class);


    private final OrderRepositoryPort orderRepository;

    public OrderStatusProjectorImpl(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void projectOrderAccepted(UUID orderId) {
        Order order = orderRepository.findById(new OrderId(orderId))
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.getStatus() == OrderStatus.ACCEPTED
                || order.getStatus() == OrderStatus.REJECTED
                || order.getStatus() == OrderStatus.DECLINED) {
            log.debug("⚠️ Order {} already in {} status, skipping", orderId, order.getStatus());
            return;
        }
        // Domain logic handles state transition
        order.accept();
        orderRepository.save(order);

        log.debug("✅ Projected ACCEPTED for orderId={}", orderId);
    }

    @Override
    public void projectOrderReady(UUID orderId) {
        Order order = orderRepository.findById(new OrderId(orderId))
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        // ✅ Idempotent
        if (order.getStatus() == OrderStatus.READY) {
            log.debug("⚠️ Order {} already READY, skipping duplicate", orderId);
            return;
        }

        if (order.getStatus() != OrderStatus.ACCEPTED) {
            log.warn("⚠️ Cannot mark ready: order is {}", order.getStatus());
            return;
        }
        order.markReady();
        orderRepository.save(order);

        log.debug("✅ Projected READY for orderId={}", orderId);
    }

    @Override
    public void projectOrderPickedUp(UUID orderId) {
        Order order = orderRepository.findById(new OrderId(orderId))
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.getStatus() == OrderStatus.PICKED_UP) {
            log.debug("⚠️ Order {} already PICKED_UP, skipping duplicate", orderId);
            return;
        }

        if (order.getStatus() != OrderStatus.READY) {
            log.warn("⚠️ Cannot pick up: order is {}", order.getStatus());
            return;
        }
        order.markPickedUp("");
        orderRepository.save(order);

        log.debug("✅ Projected PICKED_UP for orderId={}", orderId);
    }

    @Override
    public void projectOrderLocation(UUID orderId, double lat, double lng) {
        Order order = orderRepository.findById(new OrderId(orderId))
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.getStatus() == OrderStatus.DELIVERED) {
            log.debug("⚠️ Order {} already DELIVERED, skipping duplicate", orderId);
            return;
        }

        if (order.getStatus() != OrderStatus.PICKED_UP) {
            log.warn("⚠️ Cannot deliver: order is {}", order.getStatus());
            return;
        }
        order.markDelivered();
        orderRepository.save(order);

        log.debug("📍 Updated location for orderId={} to {},{}", orderId, lat, lng);
    }

    @Override
    public void projectOrderDelivered(UUID orderId) {
        Order order = orderRepository.findById(new OrderId(orderId))
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        order.markDelivered();
        orderRepository.save(order);

        log.debug("✅ Projected DELIVERED for orderId={}", orderId);
    }
}
