package kdg.be.prog6.kdg.order.adapters.out.scheduler;

import kdg.be.prog6.kdg.order.domain.Order;
import kdg.be.prog6.kdg.order.domain.OrderStatus;
import kdg.be.prog6.kdg.order.ports.out.OrderRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderAutoDeclineScheduler {

    private static final Logger log = LoggerFactory.getLogger(OrderAutoDeclineScheduler.class);

    private final OrderRepositoryPort orderRepository;

    public OrderAutoDeclineScheduler(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }
    @Scheduled(fixedDelay = 30000)
    @Transactional
    public void autoDeclineExpiredOrders() {
        log.debug("Running auto-decline check...");

        try {
            List<Order> pendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);

            LocalDateTime now = LocalDateTime.now();
            int declinedCount = 0;

            for (Order order : pendingOrders) {
                if (order.getDecisionDeadline() != null &&
                        now.isAfter(order.getDecisionDeadline())) {

                    log.info("Auto-declining order {} - decision deadline passed", order.getId());

                    order.autoDecline();
                    orderRepository.save(order);

                    declinedCount++;
                }
            }

            if (declinedCount > 0) {
                log.info("Auto-declined {} orders that passed their decision deadline", declinedCount);
            }

        } catch (Exception e) {
            log.error("Error in auto-decline scheduler", e);
        }
    }
}
