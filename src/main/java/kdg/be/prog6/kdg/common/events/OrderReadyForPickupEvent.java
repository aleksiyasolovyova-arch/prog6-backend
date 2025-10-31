package kdg.be.prog6.kdg.common.events;

import org.jmolecules.event.types.DomainEvent;
import org.springframework.modulith.events.Externalized;

import java.time.LocalDateTime;
import java.util.UUID;

@Externalized("kdg.events::#{'restaurant.' + #this.restaurantId + '.order.accepted.v1'}")
public record OrderReadyForPickupEvent(
        UUID eventId,
        String occurredAt,
        UUID restaurantId,
        UUID orderId
) implements DomainEvent {}