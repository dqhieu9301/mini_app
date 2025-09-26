package org.example.feature.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "users")
public class User extends BaseEntity {
    @Column(unique = true, nullable = false, length = 40)
    private String username;

    @JsonIgnore
    private String password;

    @Column(nullable = false, length = 40)
    private String fullName;

    @Column(name = "total_point")
    @JsonIgnore
    @Builder.Default
    private Long totalPoint = 0L;

    @Column(name = "last_token")
    private Long lastToken;
}
