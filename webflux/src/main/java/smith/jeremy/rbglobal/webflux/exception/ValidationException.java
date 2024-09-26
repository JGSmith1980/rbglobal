package smith.jeremy.rbglobal.webflux.exception;

public class ValidationException extends Throwable {
    public ValidationException(String message) {
        super(message);
    }
}
