package kdg.be.prog6.kdg.restaurant.ports.in;

public interface DiscardDraftPort {
    void discardDraft(DiscardDishDraftCommand command);
}
