package kdg.be.prog6.kdg.order.ports.in;

import kdg.be.prog6.kdg.order.domain.OrderId;

public interface PlaceOrderPort {
    OrderId placeOrder(PlaceOrderCommand command);
}
