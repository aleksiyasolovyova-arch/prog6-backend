package kdg.be.prog6.kdg.common.events;

import org.jmolecules.event.types.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderRejectedEvent(
        UUID orderId,
        UUID restaurantId,
        String rejectionReason,
        LocalDateTime rejectedAt
) implements DomainEvent {}
