package kdg.be.prog6.kdg.restaurant.domain;

public enum DishState {
    DRAFT, //being edited, BUT not yet ready to publish
    SCHEDULED, //Ready to publish at a scheduled time
    PUBLISHED
}
