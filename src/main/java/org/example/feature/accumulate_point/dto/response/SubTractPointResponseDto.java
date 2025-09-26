package org.example.feature.accumulate_point.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class SubTractPointResponseDto {
    private UUID userId;
    private Long totalPoints;
}
