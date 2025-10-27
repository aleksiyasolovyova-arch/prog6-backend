package kdg.be.prog6.kdg.common;

public class DishNotAvailableException extends RuntimeException {
    public DishNotAvailableException(String message) {
        super(message);
    }
}
