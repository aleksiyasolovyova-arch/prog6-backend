package kdg.be.prog6.kdg.restaurant.domain.exceptions;

public class DraftNotFoundException extends RuntimeException {
    public DraftNotFoundException(String message) {
        super(message);
    }
}
