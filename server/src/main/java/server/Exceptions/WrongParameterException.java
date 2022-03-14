package server.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        value = HttpStatus.BAD_REQUEST,
        reason = "Bad parameter/request"
)
public class WrongParameterException
        extends RuntimeException {

    public WrongParameterException() {
        super();
    }
}
