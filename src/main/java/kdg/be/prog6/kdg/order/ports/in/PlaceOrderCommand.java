package kdg.be.prog6.kdg.order.ports.in;

import java.util.List;
import java.util.UUID;

public record PlaceOrderCommand(
        UUID customerId,
        UUID restaurantId,
        List<BasketItem> items
) {
    public record BasketItem(UUID dishId, int quantity) {}
}
