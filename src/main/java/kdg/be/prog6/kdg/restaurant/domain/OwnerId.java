package kdg.be.prog6.kdg.restaurant.domain;

import kdg.be.prog6.kdg.restaurant.domain.exceptions.OwnerNotFoundException;

import java.util.UUID;

public record OwnerId(UUID uuid) {
    public OwnerId() {this(UUID.randomUUID());}
    public void notFound() throws OwnerNotFoundException {
        throw new OwnerNotFoundException(String.format("Owner with UUID %s was not found", this.uuid));
    }
    public static OwnerId from(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Owner ID cannot be blank");
        }
        try {
            return new OwnerId(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format for Owner ID: " + id, e);
        }
    }

    public String asString() {
        return uuid.toString();
    }
}
