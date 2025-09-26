package org.example.feature.accumulate_point.repository;

import org.example.feature.accumulate_point.entity.CheckinPointConfig;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CheckinPointConfigRepository extends CrudRepository<CheckinPointConfig, UUID> {
    @Query("select c.point from CheckinPointConfig c where c.dayIndex =:dayIndex")
    Integer getPointByDayIndex(@Param("dayIndex") Integer dayIndex);

    @Query("select c from CheckinPointConfig c order by c.dayIndex asc")
    List<CheckinPointConfig> getAll();
}
