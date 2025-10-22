package kdg.be.prog6.kdg.restaurant.domain.exceptions;

public class NoDraftsToPublishException extends RuntimeException {
    public NoDraftsToPublishException(String message) {
        super(message);
    }
}
