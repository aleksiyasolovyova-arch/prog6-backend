package kdg.be.prog6.kdg.order.adapters.in.request;

import kdg.be.prog6.kdg.order.ports.in.PlaceOrderCommand;

import java.util.List;
import java.util.UUID;

public record PlaceOrderDto(
        String customerName,
        String customerEmail,
        DeliveryAddressDto deliveryAddress,
        UUID restaurantId,
        List<BasketItemDto> items
) {
    public record DeliveryAddressDto(
            String street,
            String number,
            String postalCode,
            String city,
            String country
    ) {}

    public record BasketItemDto(
            UUID dishId,
            int quantity
    ) {}

    public PlaceOrderCommand toCommand() {
        return new PlaceOrderCommand(
                this.customerName,
                this.customerEmail,
                new PlaceOrderCommand.DeliveryAddressCommand(
                        this.deliveryAddress.street,
                        this.deliveryAddress.number,
                        this.deliveryAddress.postalCode,
                        this.deliveryAddress.city,
                        this.deliveryAddress.country
                ),
                this.restaurantId,
                this.items.stream()
                        .map(item -> new PlaceOrderCommand.BasketItem(item.dishId, item.quantity))
                        .toList()
        );
    }
}
