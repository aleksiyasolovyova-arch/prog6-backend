package kdg.be.prog6.kdg.order.core;

import jakarta.transaction.Transactional;
import kdg.be.prog6.kdg.common.DishNotAvailableException;
import kdg.be.prog6.kdg.order.domain.*;
import kdg.be.prog6.kdg.order.ports.in.PlaceOrderCommand;
import kdg.be.prog6.kdg.order.ports.in.PlaceOrderPort;
import kdg.be.prog6.kdg.order.ports.out.DishView;
import kdg.be.prog6.kdg.order.ports.out.OrderRepositoryPort;
import kdg.be.prog6.kdg.order.ports.out.RestaurantMenuView;
import kdg.be.prog6.kdg.order.ports.out.RestaurantQueryPort;
import kdg.be.prog6.kdg.common.DishNotFoundException;
import kdg.be.prog6.kdg.common.RestaurantNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaceOrderUseCaseImpl implements PlaceOrderPort {
    private final OrderRepositoryPort orderRepository;
    private final RestaurantQueryPort restaurantQueryPort;

    public PlaceOrderUseCaseImpl(OrderRepositoryPort orderRepository, RestaurantQueryPort restaurantQueryPort) {
        this.orderRepository = orderRepository;
        this.restaurantQueryPort = restaurantQueryPort;
    }

    @Override
    @Transactional
    public OrderId placeOrder(PlaceOrderCommand cmd) {
        RestaurantMenuView menu = restaurantQueryPort.getMenu(cmd.restaurantId());
        if (menu == null) {
            throw new RestaurantNotFoundException("Restaurant not found");
        }

        var orderLines = cmd.items().stream()
                .map(item -> validateAndSnapshot(menu, item))
                .collect(Collectors.toList());

        CustomerInfo customerInfo = new CustomerInfo(
                cmd.customerName(),
                new DeliveryAddress(
                        cmd.deliveryAddress().street(),
                        cmd.deliveryAddress().number(),
                        cmd.deliveryAddress().postalCode(),
                        cmd.deliveryAddress().city(),
                        cmd.deliveryAddress().country()
                ),
                cmd.customerEmail()
        );
        Order order = Order.create(
                OrderId.generate(),
                customerInfo,
                cmd.restaurantId(),
                menu.restaurantName(),
                orderLines
        );
        orderRepository.save(order);
        return order.getId();
    }

    private OrderLine validateAndSnapshot(RestaurantMenuView menu, PlaceOrderCommand.BasketItem item) {
        DishView dish = menu.findDish(item.dishId());

        if (dish == null) {
            throw new DishNotFoundException("Dish was removed from menu");
        }
        if (!dish.availableForOrder()) {
            throw new DishNotAvailableException("Dish is out of stock");
        }

        return OrderLine.create(
                dish.dishId(),
                dish.name(),
                dish.price(),
                item.quantity()
        );
    }
}
