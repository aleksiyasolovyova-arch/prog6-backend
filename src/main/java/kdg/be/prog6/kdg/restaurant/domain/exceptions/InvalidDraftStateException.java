package kdg.be.prog6.kdg.restaurant.domain.exceptions;

public class InvalidDraftStateException extends RuntimeException {
    public InvalidDraftStateException(String message) {
        super(message);
    }
}
