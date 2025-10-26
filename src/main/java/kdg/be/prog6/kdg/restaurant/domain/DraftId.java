package kdg.be.prog6.kdg.restaurant.domain;

import java.util.UUID;

public record DraftId(UUID uuid) {
    public DraftId() {this(UUID.randomUUID());}
    public static DraftId generate() {
        return new DraftId(UUID.randomUUID());
    }

    public static DraftId from(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("DraftID ID cannot be blank");
        }
        try {
            return new DraftId(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format for Draft ID: " + id, e);
        }
    }

}
