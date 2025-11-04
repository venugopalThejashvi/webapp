package com.neu.cloud.service;

import org.springframework.http.ResponseEntity;

public interface HealthService {
    ResponseEntity<Void> getHealth();
}
