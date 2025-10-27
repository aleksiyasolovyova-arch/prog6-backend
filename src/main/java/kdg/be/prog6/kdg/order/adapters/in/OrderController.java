package kdg.be.prog6.kdg.order.adapters.in;

import kdg.be.prog6.kdg.order.core.AcceptOrderUseCaseImpl;
import kdg.be.prog6.kdg.order.ports.in.PlaceOrderPort;
import kdg.be.prog6.kdg.order.ports.out.OrderRepositoryPort;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final PlaceOrderPort placeOrderPort;
    private final AcceptOrderUseCaseImpl acceptOrderService;

    public OrderController(PlaceOrderPort placeOrderPort, AcceptOrderUseCaseImpl acceptOrderService) {
        this.placeOrderPort = placeOrderPort;
        this.acceptOrderService = acceptOrderService;
    }
}
