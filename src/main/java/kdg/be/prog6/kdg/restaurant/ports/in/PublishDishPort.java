package kdg.be.prog6.kdg.restaurant.ports.in;

import kdg.be.prog6.kdg.restaurant.domain.DishId;

public interface PublishDishPort {
    DishId publishDraft(PublishDishDraftCommand command);
}
