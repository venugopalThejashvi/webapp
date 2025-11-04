package com.neu.cloud.repo;

import com.neu.cloud.models.HealthCheck;
import com.neu.cloud.utils.TimedMetric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthCheckRepo extends JpaRepository<HealthCheck, Long> {
    @Override
    @TimedMetric(value = "database.health.add")
    <S extends HealthCheck> S save(S entity);
}
