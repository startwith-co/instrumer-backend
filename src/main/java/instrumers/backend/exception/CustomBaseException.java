package instrumers.backend.exception;

import lombok.Getter;

@Getter
public abstract class CustomBaseException extends RuntimeException {
    private final int httpStatus;

    protected CustomBaseException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}