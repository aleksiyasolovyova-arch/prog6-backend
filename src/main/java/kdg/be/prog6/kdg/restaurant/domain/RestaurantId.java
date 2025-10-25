package kdg.be.prog6.kdg.restaurant.domain;

import kdg.be.prog6.kdg.restaurant.domain.exceptions.OwnerNotFoundException;
import kdg.be.prog6.kdg.restaurant.domain.exceptions.RestaurantNotFoundException;

import java.util.UUID;

public record RestaurantId(UUID uuid) {
    public RestaurantId() {this(UUID.randomUUID());}

    public static RestaurantId generate() {
        return new RestaurantId(UUID.randomUUID());
    }

    public void notFound() throws RestaurantNotFoundException {
        throw new RestaurantNotFoundException(String.format("Restaurant with UUID %s was not found", this.uuid));
    }
}
