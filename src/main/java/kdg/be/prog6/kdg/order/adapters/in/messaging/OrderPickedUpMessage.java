package kdg.be.prog6.kdg.order.adapters.in.messaging;

import java.util.UUID;

public record OrderPickedUpMessage(
        UUID orderId,
        UUID restaurantId,
        String courierId
) {}
