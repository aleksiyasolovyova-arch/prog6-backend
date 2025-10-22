package kdg.be.prog6.kdg.restaurant.domain;

import kdg.be.prog6.kdg.restaurant.domain.exceptions.OwnerNotFoundException;

import java.util.UUID;

public record OwnerId(UUID uuid) {
    public OwnerId() {this(UUID.randomUUID());}
    public void notFound() throws OwnerNotFoundException {
        throw new OwnerNotFoundException(String.format("Owner with UUID %s was not found", this.uuid));
    }

}
