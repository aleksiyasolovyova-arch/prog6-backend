package kdg.be.prog6.kdg.restaurant.domain;

import java.util.UUID;

public record DishId(UUID uuid) {
    public DishId() {this(UUID.randomUUID());}
    public static DishId generate() {
        return new DishId(UUID.randomUUID());
    }
}
