package kdg.be.prog6.kdg.common.events;

import org.jmolecules.event.types.DomainEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderPlacedEvent(
        UUID orderId,
        UUID restaurantId,
        LocalDateTime createdAt,
        LocalDateTime decisionDeadline,
        BigDecimal totalAmount
) implements DomainEvent {}
