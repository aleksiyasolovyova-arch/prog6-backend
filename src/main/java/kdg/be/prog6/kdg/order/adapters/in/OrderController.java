package kdg.be.prog6.kdg.order.adapters.in;

import kdg.be.prog6.kdg.common.DishNotAvailableException;
import kdg.be.prog6.kdg.common.DishNotFoundException;
import kdg.be.prog6.kdg.order.adapters.in.request.PlaceOrderDto;
import kdg.be.prog6.kdg.order.adapters.in.response.OrderResponse;
import kdg.be.prog6.kdg.order.core.AcceptOrderUseCaseImpl;
import kdg.be.prog6.kdg.order.core.RejectOrderUseCaseImpl;
import kdg.be.prog6.kdg.order.domain.Order;
import kdg.be.prog6.kdg.order.domain.OrderId;
import kdg.be.prog6.kdg.order.ports.in.*;
import kdg.be.prog6.kdg.order.ports.out.OrderRepositoryPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static kdg.be.prog6.kdg.order.adapters.in.response.OrderResponse.mapToResponse;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final PlaceOrderPort placeOrderPort;
    private final AcceptOrderPort acceptOrderService;
    private final RejectOrderPort rejectOrderService;
    private final GetOrderDetailsPort orderDetailsService;
    private final GetRestaurantOrdersPort restaurantOrdersService;
    private final OrderRepositoryPort orderRepository;

    public OrderController(PlaceOrderPort placeOrderPort, AcceptOrderUseCaseImpl acceptOrderService, RejectOrderUseCaseImpl rejectOrderService, GetOrderDetailsPort orderDetailsService, GetRestaurantOrdersPort restaurantOrdersService, OrderRepositoryPort orderRepository) {
        this.placeOrderPort = placeOrderPort;
        this.acceptOrderService = acceptOrderService;
        this.rejectOrderService = rejectOrderService;
        this.orderDetailsService = orderDetailsService;
        this.restaurantOrdersService = restaurantOrdersService;
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody PlaceOrderDto dto) {
        try {
            PlaceOrderCommand cmd = dto.toCommand();
            OrderId orderId = placeOrderPort.placeOrder(cmd);
            Order order = orderRepository.findById(orderId).orElseThrow();
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(mapToResponse(order));
        } catch (DishNotFoundException | DishNotAvailableException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);  // Better: custom error response
        }
    }

    @PostMapping("/{orderId}/accept")
    public ResponseEntity<Void> acceptOrder(
            @PathVariable UUID orderId,
            @RequestParam UUID restaurantId
    ) {
        acceptOrderService.acceptOrder(
                new AcceptOrderCommand(OrderId.from(orderId), restaurantId)
        );
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{orderId}/reject")
    public ResponseEntity<Void> rejectOrder(
            @PathVariable UUID orderId,
            @RequestParam UUID restaurantId
    )
    {
        rejectOrderService.rejectOrder(
                new RejectOrderCommand(OrderId.from(orderId), restaurantId)
        );
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable UUID orderId) {
        OrderResponse response = orderDetailsService.getOrderDetails(
                OrderId.from(orderId)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<List<OrderResponse>> getRestaurantOrders(@RequestParam UUID restaurantId) {
        List<OrderResponse> orders = restaurantOrdersService.getRestaurantOrders(restaurantId);
        return ResponseEntity.ok(orders);
    }


}
