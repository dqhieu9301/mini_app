package org.example.common.config.security;

import org.example.common.constants.ErrorCodeEnum;
import org.example.common.exception.UnauthorizedException;
import org.example.feature.user.entity.UserDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
public class CurrentUser {

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST)
    public UserDetail getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetail) {
            return (UserDetail) authentication.getPrincipal();
        }
        throw new UnauthorizedException(ErrorCodeEnum.UNAUTHORIZED);
    }
}
