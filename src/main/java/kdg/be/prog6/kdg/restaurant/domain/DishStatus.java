package kdg.be.prog6.kdg.restaurant.domain;

public enum DishStatus {
    LIVE_ONLY,      // Published, no draft
    HAS_DRAFT,      // Published with pending draft changes
    DRAFT_ONLY      // New dish, not yet published
}
