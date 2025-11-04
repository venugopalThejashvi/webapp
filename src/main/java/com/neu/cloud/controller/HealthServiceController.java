package com.neu.cloud.controller;

import com.neu.cloud.service.HealthService;
import com.neu.cloud.utils.TimedMetric;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HealthServiceController {

    @Autowired
    private HealthService healthService;

    @GetMapping("/healthz")
    @TimedMetric("api.health.get")
    public ResponseEntity<Void> getHeath(HttpServletRequest request, HttpServletResponse response) {
        log.info("Received "+request.getMethod()+" to "+request.getRequestURI());
        log.info("ASSIGN084");
        if(!request.getParameterMap().isEmpty()){
            log.error("bad request to "+request.getRequestURI());
            return ResponseEntity.badRequest().build();
        }
        if (!(request.getMethod().equals("GET"))) {
            log.error(request.getMethod()+" method not supported");
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
         return healthService.getHealth();
    }

    @GetMapping("/cicd")
    public ResponseEntity<Void> getHeathCicd(HttpServletRequest request, HttpServletResponse response) {
        log.info("Received "+request.getMethod()+" to "+request.getRequestURI());
        if(!request.getParameterMap().isEmpty()){
            log.error("bad request to "+request.getRequestURI());
            return ResponseEntity.badRequest().build();
        }
        if (!(request.getMethod().equals("GET"))) {
            log.error(request.getMethod()+" method not supported");
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
         return healthService.getHealth();
    }
}
