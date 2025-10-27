package kdg.be.prog6.kdg.order.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private OrderId orderId;
    private UUID customerId;
    private UUID restaurantId;
    private String restaurantName;

    private List<OrderLine> orderLines;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime orderedAt;
    private LocalDateTime estimatedReadyAt;

    private Order() {}

    public static Order create(
            OrderId orderId,
            UUID customerId,
            UUID restaurantId,
            String restaurantName,
            List<OrderLine> orderLines
    ) {
        Order order = new Order();
        order.orderId = orderId;
        order.customerId = customerId;
        order.restaurantId = restaurantId;
        order.restaurantName = restaurantName;
        order.orderLines = new ArrayList<>(orderLines);
        order.status = OrderStatus.PENDING;
        order.totalAmount = orderLines.stream()
                .map(OrderLine::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.orderedAt = LocalDateTime.now();
        return order;
    }

    public void accept() {
        if (status != OrderStatus.PENDING) {
            throw new InvalidOrderStateException("Order is not pending");
        }
        this.status = OrderStatus.ACCEPTED;
    }

    public void reject() {
        if (status != OrderStatus.PENDING) {
            throw new InvalidOrderStateException("Order is not pending");
        }
        this.status = OrderStatus.REJECTED;
    }

    public void markReady() {
        if (status != OrderStatus.ACCEPTED) {
            throw new InvalidOrderStateException("Order is not accepted");
        }
        this.status = OrderStatus.READY;
        this.estimatedReadyAt = LocalDateTime.now();
    }

    // Getters
    public OrderId getId() { return orderId; }
    public UUID getCustomerId() { return customerId; }
    public UUID getRestaurantId() { return restaurantId; }
    public String getRestaurantName() { return restaurantName; }
    public List<OrderLine> getOrderLines() { return new ArrayList<>(orderLines); }
    public OrderStatus getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public LocalDateTime getOrderedAt() { return orderedAt; }
    public LocalDateTime getEstimatedReadyAt() { return estimatedReadyAt; }
}
