package kdg.be.prog6.kdg.order.adapters.out;

import kdg.be.prog6.kdg.common.RestaurantMenuFacade;
import kdg.be.prog6.kdg.order.ports.out.RestaurantQueryPort;
import kdg.be.prog6.kdg.order.ports.out.DishView;
import kdg.be.prog6.kdg.order.ports.out.RestaurantMenuView;
import kdg.be.prog6.kdg.common.MenuItemView;
import kdg.be.prog6.kdg.restaurant.ports.out.RestaurantMenuQueryPort;
import kdg.be.prog6.kdg.common.RestaurantBCMenuView;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantMenuAdapter implements RestaurantQueryPort {

    private final RestaurantMenuFacade restaurantMenuFacade;  //Inject facade from Shared Kernel

    RestaurantMenuAdapter(RestaurantMenuFacade restaurantMenuFacade) {
        this.restaurantMenuFacade = restaurantMenuFacade;
    }

    @Override
    public RestaurantMenuView getMenu(UUID restaurantId) {
        RestaurantBCMenuView restaurantMenu = restaurantMenuFacade.getMenu(restaurantId);

        if (restaurantMenu == null) {
            return null;
        }

        var dishViews = restaurantMenu.dishes().stream()
                .map(item -> new DishView(
                        item.dishId(),
                        item.name(),
                        item.price(),
                        item.availableForOrder()
                ))
                .collect(Collectors.toList());

        return new RestaurantMenuView(
                restaurantMenu.restaurantId(),
                restaurantMenu.restaurantName(),
                dishViews
        );
    }

    @Override
    public DishView getDish(UUID restaurantId, UUID dishId) {
        MenuItemView item = restaurantMenuFacade.getDish(restaurantId, dishId);

        if (item == null) {
            return null;
        }

        return new DishView(
                item.dishId(),
                item.name(),
                item.price(),
                item.availableForOrder()
        );
    }
}