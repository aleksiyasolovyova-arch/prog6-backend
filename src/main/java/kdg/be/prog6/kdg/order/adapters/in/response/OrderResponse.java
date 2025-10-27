package kdg.be.prog6.kdg.order.adapters.in.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        UUID restaurantId,
        String restaurantName,
        List<OrderLineResponse> items,
        String status,
        BigDecimal totalAmount,
        String orderedAt
) {
    public record OrderLineResponse(
            UUID dishId,
            String dishName,
            BigDecimal priceAtOrderTime,
            int quantity,
            BigDecimal totalPrice
    ) {}
}
