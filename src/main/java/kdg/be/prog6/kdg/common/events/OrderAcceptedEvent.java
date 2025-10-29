package kdg.be.prog6.kdg.common.events;

import org.jmolecules.event.types.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderAcceptedEvent(
        UUID orderId,
        UUID restaurantId,
        LocalDateTime acceptedAt
) implements DomainEvent {}
