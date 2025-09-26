package org.example.common.exception;

import lombok.Getter;
import org.example.common.constants.ErrorCodeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
@Getter
public class DuplicateException extends RuntimeException {
    private final ErrorCodeEnum errorCode;

    public DuplicateException(ErrorCodeEnum errorCode) {
        super();
        this.errorCode = errorCode;
    }
}
