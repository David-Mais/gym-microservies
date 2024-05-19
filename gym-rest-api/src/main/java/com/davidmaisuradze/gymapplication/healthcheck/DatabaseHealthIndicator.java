package com.davidmaisuradze.gymapplication.healthcheck;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
@RequiredArgsConstructor
public class DatabaseHealthIndicator implements HealthIndicator {
    private final DataSource dataSource;

    @Override
    public Health health() {
        String database = "database";
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT 1")) {
                if (resultSet.next() && resultSet.getInt(1) == 1) {
                    return Health.up()
                            .withDetail(database, "UP")
                            .withDetail("url", metaData.getURL())
                            .withDetail("database_name", metaData.getDatabaseProductName())
                            .withDetail("database_version", metaData.getDatabaseProductVersion())
                            .build();
                } else {
                    return Health.down()
                            .withDetail(database, "Unexpected validation query response")
                            .withDetail("url", metaData.getURL())
                            .build();
                }
            }

        } catch (Exception e) {
            return Health.down(e)
                    .withDetail(database, "DOWN")
                    .build();
        }
    }
}
