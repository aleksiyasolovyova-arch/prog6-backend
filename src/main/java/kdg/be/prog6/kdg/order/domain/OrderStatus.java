package kdg.be.prog6.kdg.order.domain;

public enum OrderStatus {
    PENDING,      // Waiting for restaurant approval
    ACCEPTED,     // Restaurant accepted
    REJECTED,     // Restaurant rejected
    READY,        // Ready for pickup
    DELIVERED     // Delivered
}
