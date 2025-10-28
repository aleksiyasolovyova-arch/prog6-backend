package kdg.be.prog6.kdg.order.adapters.in.response;

import kdg.be.prog6.kdg.order.domain.Order;

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

    public static OrderResponse mapToResponse(Order order) {
        return new OrderResponse(
                order.getId().uuid(),
                order.getRestaurantId(),
                order.getRestaurantName(),
                order.getOrderLines().stream()
                        .map(line -> new OrderResponse.OrderLineResponse(
                                line.getDishId(),
                                line.getDishName(),
                                line.getPrice(),
                                line.getQuantity(),
                                line.getTotalPrice()
                        ))
                        .toList(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getOrderedAt().toString()
        );
    }
}
