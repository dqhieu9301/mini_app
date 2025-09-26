package org.example.common.exception;

import lombok.Getter;
import org.example.common.constants.ErrorCodeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
@Getter
public class UnauthorizedException extends RuntimeException {
    private final ErrorCodeEnum errorCode;

    public UnauthorizedException(ErrorCodeEnum errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
