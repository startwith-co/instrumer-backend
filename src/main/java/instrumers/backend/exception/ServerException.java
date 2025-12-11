package instrumers.backend.exception;

import lombok.Getter;

@Getter
public class ServerException  extends CustomBaseException {
    public ServerException(int httpStatus, String message) {
        super(message, httpStatus);
    }
}
