package kdg.be.prog6.kdg.restaurant.domain;

import kdg.be.prog6.kdg.common.RestaurantNotFoundException;

import java.util.UUID;

public record RestaurantId(UUID uuid) {
    public RestaurantId() {this(UUID.randomUUID());}

    public static RestaurantId generate() {
        return new RestaurantId(UUID.randomUUID());
    }

    public static RestaurantId from(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("RestaurantID ID cannot be blank");
        }
        try {
            return new RestaurantId(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format for Restaurant ID: " + id, e);
        }
    }

    public void notFound() throws RestaurantNotFoundException {
        throw new RestaurantNotFoundException(String.format("Restaurant with UUID %s was not found", this.uuid));
    }
}
