package org.example.common.constants;

import lombok.Getter;

@Getter
public enum RedisKeyEnum {
    USER_PROFILE("user:%s:profile"),
    TOTAL_POINT_USER("total_point:%s:user"),
    RFENCEDLOCK_POINT("RFencedLock:%s:point"),
    CHECKIN_TODAY("checkin:%s:today"),
    TOTAL_DAY_CHECKEDIN("total_day:%s:checkedin"),
    DAY_CHECKIN("day_checkin")
    ;

    private final String pattern;

    RedisKeyEnum(String pattern) {
        this.pattern = pattern;
    }

    public String format(Object... args) {
        return String.format(this.pattern, args);
    }
}
