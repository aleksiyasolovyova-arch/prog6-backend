package kdg.be.prog6.kdg.order.adapters.in.request;

import java.util.List;
import java.util.UUID;

public record PlaceOrderDto(
        UUID customerId,
        UUID restaurantId,
        List<BasketItemDto> items
) {
    public record BasketItemDto(
            UUID dishId,
            int quantity
    ) {}
}
