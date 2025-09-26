package org.example.feature.accumulate_point.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.common.base.BaseEntity;
import org.example.common.constants.ChangeTypePointEnum;
import org.example.feature.user.entity.User;

@EqualsAndHashCode(callSuper = true)
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "accumulate_point_history")
public class AccumulatePointHistory extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "change_type")
    private ChangeTypePointEnum changeType;

    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id", columnDefinition = "uuid")
    @JsonIgnore
    private User user;
}
