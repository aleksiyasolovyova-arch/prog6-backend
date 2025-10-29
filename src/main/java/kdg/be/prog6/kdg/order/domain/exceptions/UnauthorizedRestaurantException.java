package kdg.be.prog6.kdg.order.domain.exceptions;

public class UnauthorizedRestaurantException extends RuntimeException {
    public UnauthorizedRestaurantException(String message) {
        super(message);
    }
}
