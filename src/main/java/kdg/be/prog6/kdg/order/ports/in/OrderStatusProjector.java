package kdg.be.prog6.kdg.order.ports.in;

import java.util.UUID;

public interface OrderStatusProjector {
    void projectOrderAccepted(UUID orderId);
    void projectOrderReady(UUID orderId);
    void projectOrderPickedUp(UUID orderId);
    void projectOrderLocation(UUID orderId, double lat, double lng);
    void projectOrderDelivered(UUID orderId);
}
