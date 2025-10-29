package kdg.be.prog6.kdg.restaurant.adapters.in;

import kdg.be.prog6.kdg.common.RestaurantNotFoundException;
import kdg.be.prog6.kdg.restaurant.adapters.in.request.CreateRestaurantRequest;
import kdg.be.prog6.kdg.restaurant.adapters.in.request.SchedulePublishAllDraftsRequest;
import kdg.be.prog6.kdg.restaurant.adapters.in.response.*;
import kdg.be.prog6.kdg.restaurant.core.*;
import kdg.be.prog6.kdg.restaurant.domain.DishId;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;
import kdg.be.prog6.kdg.restaurant.domain.exceptions.DishLimitExceededException;
import kdg.be.prog6.kdg.restaurant.domain.exceptions.InvalidScheduleException;
import kdg.be.prog6.kdg.restaurant.domain.exceptions.NoDraftsToPublishException;
import kdg.be.prog6.kdg.restaurant.ports.in.CreateRestaurantPort;
import kdg.be.prog6.kdg.restaurant.ports.in.SchedulePublishAllDraftsCommand;
import kdg.be.prog6.kdg.restaurant.ports.in.SchedulePublishAllDraftsPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private final CreateRestaurantPort port;
    private final BrowseRestaurantsUseCaseImpl browseRestaurantsUseCase;
    private final ViewRestaurantDetailsUseCaseImpl viewRestaurantDetailsUseCase;
    private final ViewMenuUseCaseImpl viewMenuUseCase;
    private final SchedulePublishAllDraftsPort schedulePublishAllDraftsPort;

    public RestaurantController(CreateRestaurantUseCaseImpl port, BrowseRestaurantsUseCaseImpl browseRestaurantsUseCase, ViewRestaurantDetailsUseCaseImpl viewRestaurantDetailsUseCase, ViewMenuUseCaseImpl viewMenuUseCase, SchedulePublishAllDraftsPort schedulePublishAllDraftsPort) {
        this.port = port;
        this.browseRestaurantsUseCase = browseRestaurantsUseCase;
        this.viewRestaurantDetailsUseCase = viewRestaurantDetailsUseCase;
        this.viewMenuUseCase = viewMenuUseCase;
        this.schedulePublishAllDraftsPort = schedulePublishAllDraftsPort;
    }

    @PostMapping
    public ResponseEntity<RestaurantIdResponse> createRestaurant(
            @RequestBody CreateRestaurantRequest request
            ) {
        RestaurantId id = port.createRestaurant(request.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED).body(new RestaurantIdResponse(id.uuid()));
    }

    @GetMapping
    public ResponseEntity<RestaurantListResponse> browseRestaurants() {
        RestaurantListResponse response = browseRestaurantsUseCase.browseAllRestaurants();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDetailResponse> getRestaurantDetails(
            @PathVariable UUID restaurantId
    ) {
        RestaurantDetailResponse response = viewRestaurantDetailsUseCase.getRestaurantDetails(RestaurantId.from(restaurantId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{restaurantId}/menu")
    public ResponseEntity<MenuResponse> getMenu(
            @PathVariable UUID restaurantId
    ) {
        MenuResponse response = viewMenuUseCase.getMenu(
                RestaurantId.from(restaurantId));
        return ResponseEntity.ok(response);
    }

}
