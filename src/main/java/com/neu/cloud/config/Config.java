package com.neu.cloud.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@Slf4j
public class Config extends OncePerRequestFilter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!isDatabaseConnected()) {
            log.error("Database not connected");
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);  // 503 Service Unavailable
            return;
        }

        if(request.getRequestURI().equals("/v1/file")) {
            if(request.getMethod().equals("GET") || request.getMethod().equals("DELETE")) {
                log.error(request.getRequestURI()+" "+request.getMethod()+ " method not supported");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("X-Content-Type-Options", "nosniff");
        if ("GET".equals(request.getMethod()) && request.getContentLength() > 0) {
            log.error(request.getRequestURI()+" "+request.getMethod()+ " method not supported");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (request.getMethod().equals("OPTIONS")){
            log.error(request.getRequestURI()+" "+request.getMethod()+ " method not supported");
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
        filterChain.doFilter(request, response);
    }

    private boolean isDatabaseConnected() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(2);  // 2-second timeout
        } catch (SQLException e) {
            logger.warn("Database connection check failed: {}");
            return false;
        }
    }
}
