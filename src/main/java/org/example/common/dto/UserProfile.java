package org.example.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserProfile {
    private UUID id;
    private String username;
}
