package kdg.be.prog6.kdg.order.ports.out;

import java.util.UUID;

public interface RestaurantQueryPort {
    DishView getDish(UUID restaurantId, UUID dishId);
    RestaurantMenuView getMenu(UUID restaurantId);
}
