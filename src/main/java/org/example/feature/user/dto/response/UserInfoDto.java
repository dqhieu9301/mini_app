package org.example.feature.user.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserInfoDto {
    private UUID id;
    private String fullName;
    private String username;
    private Long totalPoint;
}
