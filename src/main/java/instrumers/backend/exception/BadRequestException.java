package instrumers.backend.exception;

import lombok.Getter;

@Getter
public class BadRequestException  extends CustomBaseException {
    public BadRequestException(int httpStatus, String message) {
        super(message, httpStatus);
    }
}
