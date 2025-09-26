package org.example.feature.accumulate_point.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckInDayResponseDto {
    private Integer day;
    private Boolean isCheckIn;
}
