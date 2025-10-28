package kdg.be.prog6.kdg.common;

import java.util.List;
import java.util.UUID;

public record RestaurantBCMenuView(UUID restaurantId,
                                   String restaurantName,
                                   List<MenuItemView> dishes
) {
    public MenuItemView findDish(UUID dishId) {
        return dishes.stream()
                .filter(d -> d.dishId().equals(dishId))
                .findFirst()
                .orElse(null);
    }
}
