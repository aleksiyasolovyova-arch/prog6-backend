package kdg.be.prog6.kdg.order.domain;

import java.util.UUID;

public record OrderId(UUID uuid) {
    public OrderId() {this(UUID.randomUUID());}

    public static OrderId generate() {
        return new OrderId(UUID.randomUUID());
    }

    public static OrderId from(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("OrderID ID cannot be blank");
        }
        return new OrderId(id);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof OrderId) {
            return uuid.equals(((OrderId)other).uuid);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
