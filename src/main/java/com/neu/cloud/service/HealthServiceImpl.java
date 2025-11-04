package com.neu.cloud.service;

import com.neu.cloud.repo.HealthCheckRepo;
import com.neu.cloud.models.HealthCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HealthServiceImpl implements HealthService{

    @Autowired
    private HealthCheckRepo healthCheckRepo;

    @Override
    public ResponseEntity<Void> getHealth() {
        healthCheckRepo.save(new HealthCheck());
        log.info("Health check uploaded");
        return ResponseEntity.ok().build();
    }
}
