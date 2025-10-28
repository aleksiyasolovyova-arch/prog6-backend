package kdg.be.prog6.kdg.common;

import java.math.BigDecimal;
import java.util.UUID;

public record MenuItemView(UUID dishId,
                           String name,
                           BigDecimal price,
                           boolean availableForOrder
) {}
