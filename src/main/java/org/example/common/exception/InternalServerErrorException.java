package org.example.common.exception;

import lombok.Getter;
import org.example.common.constants.ErrorCodeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@Getter
public class InternalServerErrorException extends RuntimeException {
    private final ErrorCodeEnum errorCode;

    public InternalServerErrorException(ErrorCodeEnum errorCode) {
        super();
        this.errorCode = errorCode;
    }
}
