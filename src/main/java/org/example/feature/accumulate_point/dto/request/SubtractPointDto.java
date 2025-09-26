package org.example.feature.accumulate_point.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubtractPointDto {
    @NotNull
    @Min(1)
    private Integer point;
}
