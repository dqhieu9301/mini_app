package org.example.feature.accumulate_point.repository;

import org.example.feature.accumulate_point.entity.AccumulatePointHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface AccumulatePointHistoryRepository extends CrudRepository<AccumulatePointHistory, UUID> {
    @Query(value = """
        SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END
        FROM accumulate_point_history
         WHERE user_id = :userId
            AND DATE(created_at) = CURRENT_DATE
        """, nativeQuery = true)
    boolean existsTodayRecord(@Param("userId") UUID userId);

    @Query(value = """
        SELECT * FROM accumulate_point_history
        WHERE user_id = :userId
          AND change_type = 'INCREASE'
          AND created_at BETWEEN :startTime AND :endTime
        ORDER BY created_at DESC
        LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<AccumulatePointHistory> findIncreaseHistoryByUserAndDate(
            @Param("userId") UUID userId,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    @Query(value = """
        SELECT COUNT(*)
        FROM accumulate_point_history
        WHERE user_id = :userId
        AND change_type = 'INCREASE'
        AND created_at >= date_trunc('month', now())
        AND created_at <  date_trunc('month', now()) + interval '1 month'
        """, nativeQuery = true)
    Integer countCheckinDaysInMonth(@Param("userId") UUID userId);

}
