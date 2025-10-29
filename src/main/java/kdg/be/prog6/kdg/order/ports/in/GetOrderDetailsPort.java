package kdg.be.prog6.kdg.order.ports.in;

import kdg.be.prog6.kdg.order.adapters.in.response.OrderResponse;
import kdg.be.prog6.kdg.order.domain.Order;
import kdg.be.prog6.kdg.order.domain.OrderId;

public interface GetOrderDetailsPort {
    OrderResponse getOrderDetails(OrderId orderId);
}
