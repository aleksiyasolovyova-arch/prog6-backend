package kdg.be.prog6.kdg.common.events;

import org.jmolecules.event.types.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderPickedUpEvent(
        UUID orderId,
        UUID restaurantId,
        LocalDateTime pickedUpAt,
        String courierId
) implements DomainEvent {}