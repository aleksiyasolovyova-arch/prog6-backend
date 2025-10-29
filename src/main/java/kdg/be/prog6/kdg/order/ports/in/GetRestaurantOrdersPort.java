package kdg.be.prog6.kdg.order.ports.in;

import kdg.be.prog6.kdg.order.adapters.in.response.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface GetRestaurantOrdersPort {
    List<OrderResponse> getRestaurantOrders(UUID restaurantId);
}
