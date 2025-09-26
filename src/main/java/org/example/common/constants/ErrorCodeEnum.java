package org.example.common.constants;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {
    USERNAME_EXISTS(1, "Username already exists"),
    USERNAME_OR_PASSWORD_ERROR(2, "Username or password error"),
    UNAUTHORIZED(3, "Unauthorized"),
    INTERNAL_SERVER_ERROR(4, "Internal server error"),
    TODAY_HAS_BEEN_CHECKIN (5, "Today has been checkin"),
    MOTH_HAS_HAD_ENOUGH_CHECKINS (6, "Moth has had enough checkins"),
    OUTSIDE_CHECKIN_HOURS (7, "Outside checkin hours"),
    USER_NOT_FOUND (8, "User not found"),
    SUBTRACT_POINT_OVER_TOTAL_POINT (9, "Subtract point over total point"),
    NOT_EXIST_DAY_ACCUMULATED_POINT (10, "Not exist day accumulate point"),
    ;

    private final int code;
    private final String message;

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
