package org.example.feature.user.service;

import lombok.RequiredArgsConstructor;
import org.example.common.config.security.CurrentUser;
import org.example.common.constants.ErrorCodeEnum;
import org.example.common.dto.UserProfile;
import org.example.common.exception.BadRequestException;
import org.example.common.exception.DuplicateException;
import org.example.common.exception.NotFoundException;
import org.example.common.util.JwtUtil;
import org.example.feature.user.dto.request.LoginUserDto;
import org.example.feature.user.dto.request.RegisterUserDto;
import org.example.feature.user.dto.response.LoginResponseDto;
import org.example.feature.user.entity.User;
import org.example.feature.user.mapper.UserMapper;
import org.example.feature.user.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUser currentUser;
    private final JwtUtil jwtUtil;

    public Object register(RegisterUserDto registerUserDto) {
        User user = userRepository.findByUsername(registerUserDto.getUsername().toLowerCase());
        if(user != null) throw new DuplicateException(ErrorCodeEnum.USERNAME_EXISTS);
        try {
            user = userRepository.save(User.builder()
                    .username(registerUserDto.getUsername().toLowerCase())
                    .password(passwordEncoder.encode(registerUserDto.getPassword()))
                    .fullName(registerUserDto.getFullName())
                    .build());

            return UserMapper.toUserInfoDto(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateException(ErrorCodeEnum.USERNAME_EXISTS);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Object login(LoginUserDto loginUserDto) {
        User user = userRepository.findByUsername(loginUserDto.getUsername());
        if(user == null || !passwordEncoder.matches(loginUserDto.getPassword(), user.getPassword()))
            throw new BadRequestException(ErrorCodeEnum.USERNAME_OR_PASSWORD_ERROR);

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("fullName", user.getFullName());

        String accessToken = jwtUtil.generateToken(user.getId(), claims);
        return LoginResponseDto.builder()
                .access_token(accessToken)
                .build();
    }

    public Object profile() {
        UserProfile userProfile = currentUser.getCurrentUser().getUser();
        User user = userRepository.findById(userProfile.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCodeEnum.USER_NOT_FOUND));
        return UserMapper.toUserInfoDto(user);
    }
}
