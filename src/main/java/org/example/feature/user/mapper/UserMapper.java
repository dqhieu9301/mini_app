package org.example.feature.user.mapper;

import org.example.feature.user.dto.response.UserInfoDto;
import org.example.feature.user.entity.User;

public class UserMapper {
    public static UserInfoDto toUserInfoDto(User user) {
        return UserInfoDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .totalPoint(user.getTotalPoint())
                .build();
    }
}
