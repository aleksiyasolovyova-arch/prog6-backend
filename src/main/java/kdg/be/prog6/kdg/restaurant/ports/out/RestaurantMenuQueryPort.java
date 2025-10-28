package kdg.be.prog6.kdg.restaurant.ports.out;

import kdg.be.prog6.kdg.common.MenuItemView;
import kdg.be.prog6.kdg.common.RestaurantBCMenuView;

import java.util.UUID;

public interface RestaurantMenuQueryPort {
    RestaurantBCMenuView getMenu(UUID restaurantId);
    MenuItemView getDish(UUID restaurantId, UUID dishId);
}
