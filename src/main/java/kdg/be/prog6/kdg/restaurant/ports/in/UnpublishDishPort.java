package kdg.be.prog6.kdg.restaurant.ports.in;

public interface UnpublishDishPort {
    void unpublishDraft(UnpublishDishCommand command);
}
