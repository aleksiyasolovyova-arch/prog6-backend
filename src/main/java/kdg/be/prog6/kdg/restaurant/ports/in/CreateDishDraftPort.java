package kdg.be.prog6.kdg.restaurant.ports.in;

import kdg.be.prog6.kdg.restaurant.domain.DraftId;

public interface CreateDishDraftPort {
    DraftId createDishDraft(CreateDishDraftCommand command);
}
