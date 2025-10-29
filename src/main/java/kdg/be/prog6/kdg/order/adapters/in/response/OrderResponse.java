// order/adapters/in/response/OrderResponse.java
package kdg.be.prog6.kdg.order.adapters.in.response;

import kdg.be.prog6.kdg.order.domain.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,

        // Customer Info
        String customerName,
        String customerEmail,
        DeliveryAddressResponse deliveryAddress,

        // Restaurant Info
        UUID restaurantId,
        String restaurantName,

        // Order Details
        List<OrderLineResponse> items,
        String status,
        BigDecimal totalAmount,
        String orderedAt,
        String estimatedReadyAt
) {
    public record OrderLineResponse(
            UUID dishId,
            String dishName,
            BigDecimal priceAtOrderTime,
            int quantity,
            BigDecimal totalPrice
    ) {}

    public record DeliveryAddressResponse(
            String street,
            String number,
            String postalCode,
            String city,
            String country
    ) {}

    public static OrderResponse mapToResponse(Order order) {
        return new OrderResponse(
                order.getId().uuid(),

                // Customer info
                order.getCustomerInfo().name(),
                order.getCustomerInfo().email(),
                new DeliveryAddressResponse(
                        order.getCustomerInfo().address().street(),
                        order.getCustomerInfo().address().number(),
                        order.getCustomerInfo().address().postalCode(),
                        order.getCustomerInfo().address().city(),
                        order.getCustomerInfo().address().country()
                ),

                // Restaurant info
                order.getRestaurantId(),
                order.getRestaurantName(),

                // Order details
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
                order.getOrderedAt().toString(),
                order.getEstimatedReadyAt() != null ? order.getEstimatedReadyAt().toString() : null
        );
    }
}
