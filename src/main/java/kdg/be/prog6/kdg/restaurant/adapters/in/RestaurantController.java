package kdg.be.prog6.kdg.restaurant.adapters.in;

import kdg.be.prog6.kdg.restaurant.adapters.in.request.CreateRestaurantRequest;
import kdg.be.prog6.kdg.restaurant.core.CreateRestaurantUseCaseImpl;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;
import kdg.be.prog6.kdg.restaurant.ports.in.CreateRestaurantCommand;
import kdg.be.prog6.kdg.restaurant.ports.in.CreateRestaurantPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private final CreateRestaurantPort port;

    public RestaurantController(CreateRestaurantUseCaseImpl port) {
        this.port = port;
    }

    @PostMapping
    public ResponseEntity<RestaurantId> createRestaurant(
            @RequestBody CreateRestaurantRequest request
            ) {
        RestaurantId id = port.createRestaurant(request.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }
}
