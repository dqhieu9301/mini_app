package org.example.feature.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.feature.user.dto.request.LoginUserDto;
import org.example.feature.user.dto.request.RegisterUserDto;
import org.example.feature.user.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/api/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public Object register(@RequestBody @Validated RegisterUserDto registerUserDto) {
        return userService.register(registerUserDto);
    }

    @PostMapping("/login")
    public Object login(@RequestBody @Validated LoginUserDto loginUserDto) {
        return userService.login(loginUserDto);
    }

    @GetMapping()
    public Object profile() {
        return userService.profile();
    }
}
