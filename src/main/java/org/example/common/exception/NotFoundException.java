package org.example.common.exception;

import lombok.Getter;
import org.example.common.constants.ErrorCodeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class NotFoundException extends RuntimeException {
    private final ErrorCodeEnum errorCode;

    public NotFoundException(ErrorCodeEnum errorCode) {
        super();
        this.errorCode = errorCode;
    }
}
