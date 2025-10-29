package kdg.be.prog6.kdg.common.events;

import org.jmolecules.event.types.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderAutoDeclinedEvent(
        UUID orderId,
        UUID restaurantId,
        LocalDateTime declinedAt
) implements DomainEvent {}
