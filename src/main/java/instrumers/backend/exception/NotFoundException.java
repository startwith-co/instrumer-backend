package instrumers.backend.exception;

import lombok.Getter;

@Getter
public class NotFoundException  extends CustomBaseException {
    public NotFoundException(int httpStatus, String message) {
        super(message, httpStatus);
    }
}
