package kdg.be.prog6.kdg.order.adapters.out.persistence;

import kdg.be.prog6.kdg.order.domain.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {
    public OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId().uuid());

        // Map customer info
        entity.setCustomerName(order.getCustomerInfo().name());
        entity.setCustomerEmail(order.getCustomerInfo().email());
        entity.setDeliveryStreet(order.getCustomerInfo().address().street());
        entity.setDeliveryNumber(order.getCustomerInfo().address().number());
        entity.setDeliveryPostalCode(order.getCustomerInfo().address().postalCode());
        entity.setDeliveryCity(order.getCustomerInfo().address().city());
        entity.setDeliveryCountry(order.getCustomerInfo().address().country());

        // Map restaurant info
        entity.setRestaurantId(order.getRestaurantId());
        entity.setRestaurantName(order.getRestaurantName());

        // Map order details
        entity.setStatus(order.getStatus());
        entity.setTotalAmount(order.getTotalAmount());
        entity.setOrderedAt(order.getOrderedAt());
        entity.setEstimatedReadyAt(order.getEstimatedReadyAt());

        entity.setOrderLines(
                order.getOrderLines().stream()
                        .map(this::toOrderLineEntity)
                        .collect(Collectors.toList())
        );

        return entity;
    }

    public Order toDomain(OrderEntity entity) {
        CustomerInfo customerInfo = new CustomerInfo(
                entity.getCustomerName(),
                new DeliveryAddress(
                        entity.getDeliveryStreet(),
                        entity.getDeliveryNumber(),
                        entity.getDeliveryPostalCode(),
                        entity.getDeliveryCity(),
                        entity.getDeliveryCountry()
                ),
                entity.getCustomerEmail()
        );

        return Order.reconstitute(
                OrderId.from(entity.getId()),
                customerInfo,
                entity.getRestaurantId(),
                entity.getRestaurantName(),
                entity.getOrderLines().stream()
                        .map(this::toOrderLineDomain)
                        .collect(Collectors.toList()),
                entity.getStatus(),
                entity.getTotalAmount(),
                entity.getOrderedAt(),
                entity.getEstimatedReadyAt()
        );
    }

    private OrderLineEntity toOrderLineEntity(OrderLine line) {
        OrderLineEntity entity = new OrderLineEntity();
        entity.setDishId(line.getDishId());
        entity.setDishName(line.getDishName());
        entity.setPriceAtOrderTime(line.getPrice());
        entity.setQuantity(line.getQuantity());
        return entity;
    }

    private OrderLine toOrderLineDomain(OrderLineEntity entity) {
        return OrderLine.reconstitute(
                entity.getDishId(),
                entity.getDishName(),
                entity.getPriceAtOrderTime(),
                entity.getQuantity()
        );
    }
}
