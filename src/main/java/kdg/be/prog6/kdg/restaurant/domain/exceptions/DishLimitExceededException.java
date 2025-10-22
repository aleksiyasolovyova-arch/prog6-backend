package kdg.be.prog6.kdg.restaurant.domain.exceptions;

public class DishLimitExceededException extends RuntimeException {
    public DishLimitExceededException(String message) {
        super(message);
    }
}
