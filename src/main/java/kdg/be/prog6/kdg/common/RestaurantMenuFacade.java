package kdg.be.prog6.kdg.common;

import java.util.UUID;

public interface RestaurantMenuFacade {
    RestaurantBCMenuView getMenu(UUID restaurantId);
    MenuItemView getDish(UUID restaurantId, UUID dishId);
}
