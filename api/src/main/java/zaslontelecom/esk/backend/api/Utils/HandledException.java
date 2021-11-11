package zaslontelecom.esk.backend.api.Utils;

public class HandledException extends RuntimeException {
    private String code;

    public HandledException(String message) {
        super(message);
    }

    public HandledException(String message, Throwable cause) {
        super(message, cause);
    }
}

