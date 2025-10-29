package kdg.be.prog6.kdg.order.adapters.in.messaging;

import java.util.UUID;

public record OrderDeliveredMessage(
        UUID orderId,
        UUID restaurantId
) {}
