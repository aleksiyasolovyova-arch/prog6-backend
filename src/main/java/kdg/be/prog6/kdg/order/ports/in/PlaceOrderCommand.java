package kdg.be.prog6.kdg.order.ports.in;

import java.util.List;
import java.util.UUID;

public record PlaceOrderCommand(
        String customerName,
        String customerEmail,
        DeliveryAddressCommand deliveryAddress,
        UUID restaurantId,
        List<BasketItem> items
) {
    public record DeliveryAddressCommand(
            String street,
            String number,
            String postalCode,
            String city,
            String country
    ) {}

    public record BasketItem(
            UUID dishId,
            int quantity
    ) {}
}
