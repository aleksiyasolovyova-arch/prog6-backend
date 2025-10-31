package kdg.be.prog6.kdg.order.domain;

import kdg.be.prog6.kdg.common.events.*;
import kdg.be.prog6.kdg.order.domain.exceptions.InvalidOrderStateException;
import org.jmolecules.event.types.DomainEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private OrderId orderId;
    private CustomerInfo customerInfo;
    private UUID restaurantId;
    private String restaurantName;

    private List<OrderLine> orderLines;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime orderedAt;
    private LocalDateTime estimatedReadyAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime readyAt;
    private String rejectionReason;
    private LocalDateTime decisionDeadline;

    private List<DomainEvent> events = new ArrayList<>();

    private Order() {}

    public static Order create(
            OrderId orderId,
            CustomerInfo customerInfo,
            UUID restaurantId,
            String restaurantName,
            List<OrderLine> orderLines
    ) {
        Order order = new Order();
        order.orderId = orderId;
        order.customerInfo = customerInfo;
        order.restaurantId = restaurantId;
        order.restaurantName = restaurantName;
        order.orderLines = new ArrayList<>(orderLines);
        order.status = OrderStatus.PENDING;
        order.totalAmount = orderLines.stream()
                .map(OrderLine::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.orderedAt = LocalDateTime.now();
        order.decisionDeadline = LocalDateTime.now().plusMinutes(5);
        order.registerEvent(new OrderPlacedEvent(
                order.orderId.uuid(),
                restaurantId,
                order.orderedAt,
                order.decisionDeadline,
                order.totalAmount
        ));

        return order;
    }

    public void accept() {
        if (status != OrderStatus.PENDING) {
            throw new InvalidOrderStateException("Order is not pending");
        }
        this.status = OrderStatus.ACCEPTED;
        this.acceptedAt = LocalDateTime.now();

        // Build event payload matching new OrderAcceptedEvent signature
        UUID eventId = UUID.randomUUID();
        String occurredAt = this.acceptedAt.toString();

        // Map dropoff (customer) address
        AddressDto dropoffAddress = new AddressDto(
                this.customerInfo.address().street(),
                this.customerInfo.address().number(),
                this.customerInfo.address().postalCode(),
                this.customerInfo.address().city()
        );

        // Pickup address/coordinates are not available in Order domain; pass nulls for now
        PickupAddressDto pickupAddress = null;
        CoordinatesDto pickUpCoordinates = null;
        CoordinatesDto dropoffCoordinates = null;

        registerEvent(new OrderAcceptedEvent(
                eventId,
                this.orderId.uuid(),
                occurredAt,
                this.restaurantId,
                pickupAddress,
                pickUpCoordinates,
                dropoffAddress,
                dropoffCoordinates
        ));
    }

    public void reject(String reason) {
        if (status != OrderStatus.PENDING) {
            throw new InvalidOrderStateException("Order is not pending");
        }
        this.status = OrderStatus.REJECTED;
        this.rejectionReason = reason;

        registerEvent(new OrderRejectedEvent(
                this.orderId.uuid(),
                this.restaurantId,
                reason,
                LocalDateTime.now()
        ));
    }

    /**
     * Called by a scheduled job/service after 5 minutes
     * if no accept/reject decision was made
     */
    public void autoDecline() {
        if (status != OrderStatus.PENDING) {
            throw new InvalidOrderStateException("Order is not pending");
        }
        this.status = OrderStatus.DECLINED;

        registerEvent(new OrderAutoDeclinedEvent(
                this.orderId.uuid(),
                this.restaurantId,
                LocalDateTime.now()
        ));
    }

    public void markReady() {
        if (status != OrderStatus.ACCEPTED) {
            throw new InvalidOrderStateException("Order is not accepted");
        }
        this.status = OrderStatus.READY;
        this.readyAt = LocalDateTime.now();
        this.estimatedReadyAt = this.readyAt;

        // Build event payload matching new OrderReadyForPickupEvent signature
        UUID eventId = UUID.randomUUID();
        String occurredAt = this.readyAt.toString();
        registerEvent(new OrderReadyForPickupEvent(
                eventId,
                occurredAt,
                this.restaurantId,
                this.orderId.uuid()
        ));
    }

    /**
     * Called when delivery service picks up the order
     * Comes from RabbitMQ message
     */
    public void markPickedUp(String courierId) {
        if (status != OrderStatus.READY) {
            throw new InvalidOrderStateException("Order is not ready for pickup");
        }
        this.status = OrderStatus.PICKED_UP;

        registerEvent(new OrderPickedUpEvent(
                this.orderId.uuid(),
                this.restaurantId,
                LocalDateTime.now(),
                courierId
        ));
    }

    /**
     * Called when delivery service confirms delivery
     * Comes from RabbitMQ message
     */
    public void markDelivered() {
        if (status != OrderStatus.PICKED_UP) {
            throw new InvalidOrderStateException("Order is not picked up");
        }
        this.status = OrderStatus.DELIVERED;

        registerEvent(new OrderDeliveredEvent(
                this.orderId.uuid(),
                this.restaurantId,
                LocalDateTime.now()
        ));
    }

    /**
     * Called when a dish in the order becomes unavailable
     * during checkout or after order was placed
     */
    public void invalidate(String reason) {
        if (status.equals(OrderStatus.DELIVERED) ||
                status.equals(OrderStatus.REJECTED) ||
                status.equals(OrderStatus.INVALID)) {
            throw new InvalidOrderStateException("Cannot invalidate order in status: " + status);
        }
        this.status = OrderStatus.INVALID;

        registerEvent(new OrderInvalidatedEvent(
                this.orderId.uuid(),
                this.restaurantId,
                reason,
                LocalDateTime.now()
        ));
    }

    public static Order reconstitute(
            OrderId orderId,
            CustomerInfo customerInfo,
            UUID restaurantId,
            String restaurantName,
            List<OrderLine> orderLines,
            OrderStatus status,
            BigDecimal totalAmount,
            LocalDateTime orderedAt,
            LocalDateTime estimatedReadyAt,
            LocalDateTime decisionDeadline
    ) {
        Order order = new Order();
        order.orderId = orderId;
        order.customerInfo = customerInfo;
        order.restaurantId = restaurantId;
        order.restaurantName = restaurantName;
        order.orderLines = new ArrayList<>(orderLines);
        order.status = status;
        order.totalAmount = totalAmount;
        order.orderedAt = orderedAt;
        order.estimatedReadyAt = estimatedReadyAt;
        order.decisionDeadline = decisionDeadline;
        return order;
    }

    private void registerEvent(DomainEvent event) {
        events.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        return new ArrayList<>(events);
    }

    public void clearDomainEvents() {
        events.clear();
    }

    // Getters
    public OrderId getId() { return orderId; }
    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }
    public UUID getRestaurantId() { return restaurantId; }
    public String getRestaurantName() { return restaurantName; }
    public List<OrderLine> getOrderLines() { return new ArrayList<>(orderLines); }
    public OrderStatus getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public LocalDateTime getOrderedAt() { return orderedAt; }
    public LocalDateTime getEstimatedReadyAt() { return estimatedReadyAt; }
    public LocalDateTime getAcceptedAt() { return acceptedAt; }
    public LocalDateTime getReadyAt() { return readyAt; }
    public String getRejectionReason() { return rejectionReason; }
    public LocalDateTime getDecisionDeadline() { return decisionDeadline; }
}
