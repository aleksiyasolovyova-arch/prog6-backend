package kdg.be.prog6.kdg.restaurant.domain.exceptions;

public class DraftAlreadyExistsException extends RuntimeException {
    public DraftAlreadyExistsException(String message) {
        super(message);
    }
}
