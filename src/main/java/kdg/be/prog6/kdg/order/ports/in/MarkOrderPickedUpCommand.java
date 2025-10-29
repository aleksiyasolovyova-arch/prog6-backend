package kdg.be.prog6.kdg.order.ports.in;

import kdg.be.prog6.kdg.order.domain.OrderId;

public record MarkOrderPickedUpCommand(OrderId orderId,
                                       java.util.UUID restaurantId,
                                       String courierId
) {}
