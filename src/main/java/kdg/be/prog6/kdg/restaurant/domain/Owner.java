package kdg.be.prog6.kdg.restaurant.domain;

public class Owner {
    private OwnerId ownerId;
    private Email email;
    private RestaurantId restaurantId;

    public Owner(OwnerId ownerId, Email email, RestaurantId restaurantId) {
        this.ownerId = ownerId;
        this.email = email;
        this.restaurantId = restaurantId;
    }

    public void linkRestaurant(RestaurantId restaurantId) {
        if (this.restaurantId != null) {
            throw new IllegalStateException("Owner already has restaurant with id: " + restaurantId);
        }
        this.restaurantId = restaurantId;
    }

    public boolean hasRestaurant() {
        return this.restaurantId != null;
    }

    public OwnerId getOwnerId() {
        return ownerId;
    }

    public Email getEmail() {
        return email;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }
}
