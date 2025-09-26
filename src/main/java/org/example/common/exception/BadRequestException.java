package org.example.common.exception;

import lombok.Getter;
import org.example.common.constants.ErrorCodeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class BadRequestException extends RuntimeException {
    private final ErrorCodeEnum errorCode;

    public BadRequestException(ErrorCodeEnum errorCode) {
        super();
        this.errorCode = errorCode;
    }
}
