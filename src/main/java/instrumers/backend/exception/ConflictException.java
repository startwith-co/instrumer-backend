package instrumers.backend.exception;

import lombok.Getter;

@Getter
public class ConflictException  extends CustomBaseException {
    public ConflictException(int httpStatus, String message) {
        super(message, httpStatus);
    }
}
