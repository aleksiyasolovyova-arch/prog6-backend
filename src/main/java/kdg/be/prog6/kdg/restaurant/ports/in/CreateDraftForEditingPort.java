package kdg.be.prog6.kdg.restaurant.ports.in;

import kdg.be.prog6.kdg.restaurant.domain.DishDraft;
import kdg.be.prog6.kdg.restaurant.domain.DraftId;

public interface CreateDraftForEditingPort {
    DraftId createDraftForEditing(CreateDraftForEditingCommand command);
}
