package com.rematec.voucher.voucherbackapi.config;

import com.zaxxer.hikari.HikariDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    @Autowired
    private DataSource dataSource;

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;

            return Health.up()
                    .withDetail("activeConnections", hikariDataSource.getHikariPoolMXBean().getActiveConnections())
                    .withDetail("idleConnections", hikariDataSource.getHikariPoolMXBean().getIdleConnections())
                    .withDetail("totalConnections", hikariDataSource.getHikariPoolMXBean().getTotalConnections())
                    .withDetail("threadsAwaitingConnection", hikariDataSource.getHikariPoolMXBean().getThreadsAwaitingConnection())
                    .build();
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
}