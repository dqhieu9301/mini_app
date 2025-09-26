package org.example.feature.accumulate_point.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.example.common.base.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "checkin_point_config")
public class CheckinPointConfig extends BaseEntity {
    @Column(name = "day_index", unique = true, nullable = false)
    private Integer dayIndex;

    @Column(nullable = false)
    private Integer point;
}
