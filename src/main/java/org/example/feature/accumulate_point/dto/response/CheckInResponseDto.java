package org.example.feature.accumulate_point.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CheckInResponseDto {
    private UUID userId;
    private Long totalPoint;
}
