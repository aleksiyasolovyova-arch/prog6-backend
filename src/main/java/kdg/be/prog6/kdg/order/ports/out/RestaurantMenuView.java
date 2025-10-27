package kdg.be.prog6.kdg.order.ports.out;

import java.util.List;
import java.util.UUID;

public record RestaurantMenuView(
        UUID restaurantId,
        String restaurantName,
        List<DishView> dishes
) {
    public DishView findDish(UUID dishId) {
        return dishes.stream()
                .filter(d -> d.dishId().equals(dishId))
                .findFirst()
                .orElse(null);
    }
}
