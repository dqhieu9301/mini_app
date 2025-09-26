package org.example.feature.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserDto {
    @NotBlank
    @Size(min = 4, max = 40)
    private String username;

    @NotBlank
    @Size(max = 40)
    private String fullName;

    @NotBlank
    @Size(max = 40)
    private String password;
}
