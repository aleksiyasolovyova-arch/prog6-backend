package kdg.be.prog6.kdg.order.domain;

public enum OrderStatus {
    PENDING,        // Waiting for restaurant decision (5 min window)
    ACCEPTED,       // Restaurant accepted, kitchen preparing
    READY,          // Food ready for pickup
    PICKED_UP,      // Delivery service picked up the order
    DELIVERED,      // Order delivered to customer
    REJECTED,       // Restaurant rejected the order
    DECLINED,       // Auto-declined after 5 minutes with no response
    INVALID         // Order invalidated (dish became unavailable)
}
