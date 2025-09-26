package org.example.common.exception;

import org.example.common.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionHandler extends Throwable {
    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerBadRequestException(BadRequestException e, WebRequest req) {
        return new ErrorResponse(e.getErrorCode().getCode(), e.getErrorCode().getMessage());
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerDuplicateException(DuplicateException e, WebRequest req) {
        return new ErrorResponse(e.getErrorCode().getCode(), e.getErrorCode().getMessage());
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handlerUnauthorizedException(UnauthorizedException e, WebRequest req) {
        return new ErrorResponse(e.getErrorCode().getCode(), e.getErrorCode().getMessage());
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handlerInternalServerErrorException(InternalServerErrorException e, WebRequest req) {
        return new ErrorResponse(e.getErrorCode().getCode(), e.getErrorCode().getMessage());
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerNotFoundException(NotFoundException e, WebRequest req) {
        return new ErrorResponse(e.getErrorCode().getCode(), e.getErrorCode().getMessage());
    }
}
