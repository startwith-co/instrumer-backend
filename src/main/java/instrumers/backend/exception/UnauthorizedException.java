package instrumers.backend.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends CustomBaseException {
    public UnauthorizedException(int httpStatus, String message) {
        super(message, httpStatus);
    }
}
