package kdg.be.prog6.kdg.order.ports.in;

import kdg.be.prog6.kdg.order.domain.OrderId;

import java.util.UUID;

public record MarkReadyCommand(
        OrderId orderId,
        UUID restaurantId
) {
}
