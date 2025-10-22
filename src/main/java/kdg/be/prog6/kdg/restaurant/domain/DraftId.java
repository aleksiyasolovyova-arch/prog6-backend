package kdg.be.prog6.kdg.restaurant.domain;

import java.util.UUID;

public record DraftId(UUID uuid) {
    public static DraftId generate() {
        return new DraftId(UUID.randomUUID());
    }
}
