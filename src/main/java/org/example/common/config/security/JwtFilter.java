package org.example.common.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.common.constants.ErrorCodeEnum;
import org.example.common.dto.ErrorResponse;
import org.example.common.dto.UserProfile;
import org.example.common.util.JwtUtil;
import org.example.feature.user.entity.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = jwtUtil.resolveToken(request);
            if (jwt != null && jwtUtil.validateAccessToken(jwt)) {

                String subject = jwtUtil.getSubject(jwt);
                String username = jwtUtil.getUsername(jwt);

                UserDetail userDetail = new UserDetail(UserProfile.builder()
                        .id(UUID.fromString(subject))
                        .username(username)
                        .build());
                UsernamePasswordAuthenticationToken
                        authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(
                            new ErrorResponse(
                                    ErrorCodeEnum.UNAUTHORIZED.getCode(),
                                    ErrorCodeEnum.UNAUTHORIZED.getMessage()
                            )
                    )
            );
            return;
        }

        filterChain.doFilter(request, response);
    }
}
