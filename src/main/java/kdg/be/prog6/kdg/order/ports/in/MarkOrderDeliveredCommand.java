package kdg.be.prog6.kdg.order.ports.in;

import kdg.be.prog6.kdg.order.domain.OrderId;

public record MarkOrderDeliveredCommand(
        OrderId orderId,
        java.util.UUID restaurantId
) {}
