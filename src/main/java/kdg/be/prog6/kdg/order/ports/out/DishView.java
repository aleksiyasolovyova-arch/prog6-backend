package kdg.be.prog6.kdg.order.ports.out;

import java.math.BigDecimal;
import java.util.UUID;

public record DishView(
        UUID dishId,
        String name,
        BigDecimal price,
        boolean availableForOrder
) {
}
