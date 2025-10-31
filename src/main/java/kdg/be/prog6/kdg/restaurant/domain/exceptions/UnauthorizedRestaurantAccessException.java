package kdg.be.prog6.kdg.restaurant.domain.exceptions;

public class UnauthorizedRestaurantAccessException extends RuntimeException {
    public UnauthorizedRestaurantAccessException(String message) {
        super(message);
    }
}
